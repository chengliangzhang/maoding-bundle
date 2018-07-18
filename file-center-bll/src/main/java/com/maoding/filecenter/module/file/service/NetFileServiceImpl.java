package com.maoding.filecenter.module.file.service;

import com.maoding.common.module.companyDisk.service.CompanyDiskService;
import com.maoding.common.module.dynamic.service.DynamicService;
import com.maoding.constDefine.companyDisk.FileSizeSumType;
import com.maoding.constDefine.netFile.NetFileStatus;
import com.maoding.constDefine.netFile.NetFileType;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.core.bean.MultipartFileParam;
import com.maoding.core.exception.DataNotFoundException;
import com.maoding.filecenter.module.file.dao.NetFileDAO;
import com.maoding.filecenter.module.file.dto.DeleteDTO;
import com.maoding.filecenter.module.file.dto.DirectoryDTO;
import com.maoding.filecenter.module.file.dto.NetFileQueryDTO;
import com.maoding.filecenter.module.file.dto.NetFileRenameDTO;
import com.maoding.filecenter.module.file.model.NetFileDO;
import com.maoding.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Wuwq on 2017/4/24.
 */
@Service("netFileService")
public class NetFileServiceImpl extends BaseService implements NetFileService {

    private static final Logger log = LoggerFactory.getLogger(NetFileServiceImpl.class);

    @Autowired
    private NetFileDAO netFileDAO;

    @Autowired
    private FastdfsService fastdfsService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private CompanyDiskService companyDiskService;

    /**
     * 判断名字在同一文件夹下是否重复
     */
    private boolean duplicated(String pid, String fileName) {
        Example selectExample = new Example(NetFileDO.class);
        selectExample.createCriteria()
                .andCondition("pid = ", pid)
                .andCondition("file_name = ", fileName)
                .andCondition("status = '0' ");
        return netFileDAO.selectCountByExample(selectExample) > 0;
    }

    /**
     * 根据PID重置IDPath
     */
    private void resetSkyDrivePath(NetFileDO netFileDO) throws DataNotFoundException {
        if (StringUtils.isBlank(netFileDO.getPid())) {
            netFileDO.setSkyDrivePath(netFileDO.getId());
        } else {
            NetFileDO parent = netFileDAO.selectByPrimaryKey(netFileDO.getPid());
            if (parent == null)
                throw new DataNotFoundException(String.format("找不到父目录：%s", netFileDO.getPid()));
            netFileDO.setSkyDrivePath(String.format("%s-%s", parent.getSkyDrivePath(), netFileDO.getId()));
        }
    }

    /**
     * 创建目录
     */
    @Override
    public ApiResult createDirectory(DirectoryDTO dir) throws DataNotFoundException {
        if (StringUtils.isBlank(dir.getFileName()))
            return ApiResult.failed("文件夹名称不能为空", null);

        if (duplicated(dir.getPid(), dir.getFileName()))
            return ApiResult.failed("同级目录下已存在名称重复的项", null);

        NetFileDO netFileDO = new NetFileDO();
        BeanUtils.copyProperties(dir, netFileDO);
        List<DirectoryDTO> directoryDTOS = netFileDAO.getDirectoryDTOList(dir);
        netFileDO.initEntity();
        netFileDO.setCreateBy(dir.getAccountId());
        netFileDO.setUpdateBy(dir.getAccountId());
        netFileDO.setStatus(NetFileStatus.Normal.toString());
        netFileDO.setType(NetFileType.DIRECTORY);
        netFileDO.setIsCustomize(0);
        netFileDO.setParam4(null != directoryDTOS ? directoryDTOS.size() + 1 : 0);
        //根据PID重置IDPath
        resetSkyDrivePath(netFileDO);

        if (netFileDAO.insert(netFileDO) > 0) {
            //添加项目动态
            dynamicService.addCreateDynamic(netFileDO, dir.getCompanyId(), dir.getAccountId());
            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    //判断文件是否可写，如果操作者和文件创建者相同则可写，否则不可写
    private boolean isWritable(NetFileDO file, String accountId){
        return (StringUtils.isNotEmpty(accountId)
                && ((accountId.equals(file.getCreateBy())) || StringUtils.isEmpty(file.getCreateBy())));
    }

    //转换布尔值为字符串，0-false,1-true
    private String toString(boolean b){
        return b ? "1" : "0";
    }

    /**
     * 上传文件
     */
    @Override
    public ApiResult uploadFile(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);

        //在创建新文件（id为空)时，检查文件是否存在同名
        if (StringUtils.isEmpty(getIdFromParam(param))){
            String pid = (String) param.getParam().get("pid");
            String name = param.getFileName();
            NetFileDO file = getChildByPidAndName(pid,name);
            if (file != null){
                String accountId = (String) param.getParam().get("accountId");
                FastdfsUploadResult errorResult = new FastdfsUploadResult();
                errorResult.setNetFileId(file.getId());
                errorResult.setIsWritable(toString(isWritable(file,accountId)));
                return ApiResult.failed("存在重名文件", errorResult);
            }
        }

        long startTime = System.currentTimeMillis();
        log.info("开始时间====" + startTime);
        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);

        String accountId = (String) param.getParam().get("accountId");
        String companyId = (String) param.getParam().get("companyId");
        String projectId = (String) param.getParam().get("projectId");

        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(projectId))
            return ApiResult.failed("项目ID不能为空", null);

        //业务系统逻辑
        NetFileDO netFileDO = new NetFileDO();
        netFileDO.initEntity();
        if (getIdFromParam(param) != null){
            netFileDO.setId(getIdFromParam(param));
        }
        netFileDO.setCreateBy(accountId);
        netFileDO.setUpdateBy(accountId);
        netFileDO.setCompanyId(companyId);
        netFileDO.setProjectId(projectId);
        netFileDO.setStatus(NetFileStatus.Normal.toString());
        netFileDO.setType(getFileType(param));
        netFileDO.setIsCustomize(0);
        netFileDO.setFileGroup(fuResult.getFastdfsGroup());
        netFileDO.setFilePath(fuResult.getFastdfsPath());
        netFileDO.setFileSize(fuResult.getFileSize());
        netFileDO.setFileName(fuResult.getFileName());
        netFileDO.setFileExtName(fuResult.getFileExtName());

        netFileDO.setPid((String) param.getParam().get("pid"));

        //根据PID重置IDPath
        resetSkyDrivePath(netFileDO);

        int n;
        if (getIdFromParam(param) != null) {
            n = netFileDAO.updateByPrimaryKey(netFileDO);
        } else {
            n = netFileDAO.insert(netFileDO);
        }
        if (n > 0) {
            //计算剩余空间
            long endTime = System.currentTimeMillis();    //获取结束时间
            log.info("结束时间====" + endTime);
//            log.info("最后一次用时=====" + (endTime - startTime) + "ms");
            log.info("文件大小====" + (fuResult.getFileSize()) + "b");
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.DOCMGR, fuResult.getFileSize());
            //添加项目动态
            dynamicService.addCreateDynamic(netFileDO, companyId, accountId);
            return ApiResult.success(null, fuResult);
        }
        return ApiResult.failed(null, null);
    }

    //根据名称查找子目录
    private NetFileDO getChildByPidAndName(String parentId,String childName) {
        NetFileQueryDTO query = new NetFileQueryDTO();
        query.setPid(parentId);
        query.setFileName(childName);
        List<NetFileDO> nodeList = netFileDAO.listNetFile(query);
        return (!ObjectUtils.isEmpty(nodeList)) ? nodeList.get(0) : null;
    }


    /** 判断传入的文件是否临时文件 */
    private boolean isTempFile(MultipartFileParam param){
        final String tempFileKey = "tempFile";
        return (param != null)
                && (param.getParam() != null)
                && (param.getParam().containsKey(tempFileKey))
                && (param.getParam().get(tempFileKey) instanceof Boolean)
                && ((Boolean) param.getParam().get(tempFileKey));
    }

    /** 获取传入的文件类型 */
    private int getFileType(MultipartFileParam param){
        return (isTempFile(param)) ? NetFileType.FILE_TYPE_TEMP : NetFileType.FILE;
    }

    private String getIdFromParam(MultipartFileParam param){
        return ((param != null) && (param.getUploadId() != null)) ? param.getUploadId() : null;
    }

    /**
     * 重命名文件
     */
    @Override
    public ApiResult rename(NetFileRenameDTO dto) {
        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(dto.getId()))
            return ApiResult.failed("ID不能为空", null);

        NetFileDO netFileDO = netFileDAO.selectByPrimaryKey(dto.getId());
        if (netFileDO == null)
            return ApiResult.failed("找不到要重命名的项", null);

        if (duplicated(netFileDO.getPid(), dto.getFileName()))
            return ApiResult.failed("同级目录下已存在名称重复的项", null);

        NetFileDO updateObj = new NetFileDO();
        updateObj.setId(dto.getId());
        updateObj.setFileName(dto.getFileName());
        updateObj.setUpdateBy(dto.getAccountId());
        updateObj.setUpdateDate(LocalDateTime.now());

        if (netFileDAO.updateByPrimaryKeySelective(updateObj) > 0) {
            //添加项目动态
            dynamicService.addDynamic(netFileDO, updateObj, netFileDO.getCompanyId(), dto.getAccountId());
            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 删除文件
     */
    @Override
    public ApiResult delete(DeleteDTO dto) {
        if (com.mysql.jdbc.StringUtils.isNullOrEmpty(dto.getId()))
            return ApiResult.failed("ID不能为空", null);

        NetFileDO netFileDO = netFileDAO.selectByPrimaryKey(dto.getId());
        if (netFileDO == null)
            return ApiResult.failed("找不到要删除的项", null);

        if (netFileDO.getType().equals(NetFileType.DIRECTORY)) {
            if (netFileDO.getIsCustomize().equals(1))
                return ApiResult.failed("固定目录不允许删除", null);
            else {
                if (!netFileDO.getCreateBy().equalsIgnoreCase(dto.getAccountId()))
                    return ApiResult.failed("你没有权限进行该操作", null);
            }
        } else {
            if (!netFileDO.getCreateBy().equalsIgnoreCase(dto.getAccountId()))
                return ApiResult.failed("你没有权限进行该操作", null);
        }

        //TODO  逻辑删除，需要定时任务清理

        NetFileDO updateObj = new NetFileDO();
        updateObj.setId(dto.getId());
        updateObj.setStatus(NetFileStatus.Deleted.toString());
        updateObj.setUpdateBy(dto.getAccountId());
        updateObj.setUpdateDate(LocalDateTime.now());

        if (netFileDAO.updateByPrimaryKeySelective(updateObj) > 0) {

            if (netFileDO.getType().equals(NetFileType.FILE)) {
                //计算剩余空间
                companyDiskService.recalcSizeOnFileRemoved(netFileDO.getCompanyId(), FileSizeSumType.DOCMGR, netFileDO.getFileSize());
            }

            //添加项目动态
            dynamicService.addDeleteDynamic(netFileDO, netFileDO.getCompanyId(), dto.getAccountId());
            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    @Override
    public List<DirectoryDTO> getDirectoryDTOList(DirectoryDTO dir) {
        return netFileDAO.getDirectoryDTOList(dir);
    }
}
