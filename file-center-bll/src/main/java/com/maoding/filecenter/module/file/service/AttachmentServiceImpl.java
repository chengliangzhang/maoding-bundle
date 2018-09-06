package com.maoding.filecenter.module.file.service;

import com.google.common.collect.Lists;
import com.maoding.common.module.companyDisk.service.CompanyDiskService;
import com.maoding.constDefine.companyDisk.FileSizeSumType;
import com.maoding.constDefine.im.ImGroupType;
import com.maoding.constDefine.netFile.NetFileStatus;
import com.maoding.constDefine.netFile.NetFileType;
import com.maoding.core.base.BaseService;
import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.core.bean.MultipartFileParam;
import com.maoding.fastdfsClient.conn.FdfsWebServer;
import com.maoding.filecenter.module.file.dao.NetFileCrtDAO;
import com.maoding.filecenter.module.file.dao.NetFileDAO;
import com.maoding.filecenter.module.file.dto.*;
import com.maoding.filecenter.module.file.model.NetFileCrtDO;
import com.maoding.filecenter.module.file.model.NetFileDO;
import com.maoding.filecenter.module.im.dao.ImGroupDAO;
import com.maoding.filecenter.module.im.dto.GroupImgUpdateDTO;
import com.maoding.utils.StringUtils;
import com.maoding.utils.TraceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Wuwq on 2017/4/24.
 */
@Service("attachmentService")
public class AttachmentServiceImpl extends BaseService implements AttachmentService {

    private static final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    @Autowired
    private NetFileDAO netFileDAO;

    @Autowired
    private NetFileCrtDAO netFileCrtDAO;

    @Autowired
    private ImGroupDAO imGroupDAO;

    @Autowired
    private FastdfsService fastdfsService;

    @Autowired
    private CompanyDiskService companyDiskService;


    /**
     * 保存公司logo文件（仅Web端）
     */
    @Override
    public ApiResult saveCompanyLogo(SaveCompanyLogoDTO dto){
        //业务系统逻辑
        Example example = new Example(NetFileDO.class);
        example.createCriteria()
                .andCondition("company_id = ", dto.getCompanyId())
                .andCondition("type = ", NetFileType.COMPANY_LOGO_ATTACH)
                .andCondition("status = ", NetFileStatus.Normal);

        List<NetFileDO> logos = netFileDAO.selectByExample(example);

        if (CollectionUtils.isEmpty(logos)) {
            saveNewNetFile(dto.getCompanyId(), dto.getAccountId(), null, NetFileType.COMPANY_LOGO_ATTACH, 0, null,null, dto);
        } else {
            NetFileDO netFileDO = logos.get(0);
            try {
                //删除原来的
                fastdfsService.delete(netFileDO.getFileGroup(), netFileDO.getFilePath());
            } catch (Exception ex) {
                log.error("FastDFS 删除公司Logo发生异常（group:{} path:{}），{}", netFileDO.getFileGroup(), netFileDO.getFilePath(), ex.getMessage());
            }

            //更新新的Logo存储信息
            netFileDO.setFileGroup(dto.getFastdfsGroup());
            netFileDO.setFilePath(dto.getFastdfsPath());
            netFileDO.setFileSize(dto.getFileSize());
            netFileDO.setFileName(dto.getFileName());
            netFileDO.setFileExtName(dto.getFileExtName());
            netFileDO.setUpdateBy(dto.getAccountId());
            netFileDO.setUpdateDate(LocalDateTime.now());
            netFileDAO.updateByPrimaryKey(netFileDO);
        }

        //更新群组LOGO
        GroupImgUpdateDTO imgUpdateDTO = new GroupImgUpdateDTO();
        imgUpdateDTO.setOrgId(dto.getCompanyId());
        imgUpdateDTO.setGroupType(ImGroupType.COMPANY);
        imgUpdateDTO.setImg(dto.getFastdfsGroup() + "/" + dto.getFastdfsPath());
        imGroupDAO.updateGroupImg(imgUpdateDTO);

        return ApiResult.success("组织logo已上传成功", dto);
    }

    /**
     * 上传公司logo文件
     */
    @Override
    public ApiResult uploadCompanyLogo(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        if (param.isChunked())
            throw new UnsupportedOperationException("该上传不支持分片模式");

        FastdfsUploadResult fuResult = fastdfsService.uploadLogo(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);

        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String targetId = (String) param.getParam().get("targetId");

        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        if (StringUtils.isNullOrEmpty(targetId))
            targetId = null;

        //业务系统逻辑
        Example example = new Example(NetFileDO.class);
        example.createCriteria()
                .andCondition("company_id = ", companyId)
                .andCondition("type = ", NetFileType.COMPANY_LOGO_ATTACH)
                .andCondition("status = ", NetFileStatus.Normal);

        List<NetFileDO> logos = netFileDAO.selectByExample(example);

        if (CollectionUtils.isEmpty(logos) || !StringUtils.isNullOrEmpty(targetId)) {
            saveNewNetFile(companyId, accountId, null, NetFileType.COMPANY_LOGO_ATTACH, 0, targetId,null, fuResult);
        } else {
            NetFileDO netFileDO = logos.get(0);

            try {
                //删除原来的
                fastdfsService.delete(netFileDO.getFileGroup(), netFileDO.getFilePath());
            } catch (Exception ex) {
                log.error("FastDFS 删除公司Logo发生异常（group:{} path:{}），{}", netFileDO.getFileGroup(), netFileDO.getFilePath(), ex.getMessage());
            }

            //更新新的Logo存储信息
            netFileDO.setFileGroup(fuResult.getFastdfsGroup());
            netFileDO.setFilePath(fuResult.getFastdfsPath());
            netFileDO.setFileSize(fuResult.getFileSize());
            netFileDO.setFileName(fuResult.getFileName());
            netFileDO.setFileExtName(fuResult.getFileExtName());
            netFileDO.setUpdateBy(accountId);
            netFileDO.setUpdateDate(LocalDateTime.now());
            netFileDAO.updateByPrimaryKey(netFileDO);
        }

        //更新群组LOGO
        GroupImgUpdateDTO imgUpdateDTO = new GroupImgUpdateDTO();
        imgUpdateDTO.setOrgId(companyId);
        imgUpdateDTO.setGroupType(ImGroupType.COMPANY);
        imgUpdateDTO.setImg(fuResult.getFastdfsGroup() + "/" + fuResult.getFastdfsPath());
        imGroupDAO.updateGroupImg(imgUpdateDTO);

        //producerService.sendGroupMessage(updateGroupDestination, group);

        return ApiResult.success("组织logo已上传成功", fuResult);
    }

    /**
     * 上传公司轮播图片
     */
    @Override
    public ApiResult uploadCompanyBanner(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        if (param.isChunked())
            throw new UnsupportedOperationException("该上传不支持分片模式");
        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String _seq = (String) param.getParam().get("seq");
        int seq = 0;
        if (!StringUtils.isNullOrEmpty(_seq)) {
            seq = Integer.parseInt(_seq);
        }

        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);
        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);
        //插入新记录
        ApiResult ar = saveNewNetFile(companyId, accountId, null, NetFileType.COMPANY_BANNER_ATTACH, seq, null, null,fuResult);
        if (ar.isSuccessful()) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.OTHER, fuResult.getFileSize());
            //给出全路径
            return ApiResult.success(null,fuResult);
        }

        return ApiResult.failed(null, null);
    }

    /**
     * 调整公司轮播图片顺序
     */
    @Override
    public ApiResult orderCompanyBanner(NetFileOrderDTO dto) throws Exception {
        if (StringUtils.isNullOrEmpty(dto.getCompanyId()))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(dto.getAccountId()))
            return ApiResult.failed("账号ID不能为空", null);

        Example selectExample = new Example(NetFileDO.class);
        selectExample.createCriteria()
                .andCondition("company_id = ", dto.getCompanyId())
                .andCondition("type = ", NetFileType.COMPANY_BANNER_ATTACH)
                .andCondition("status = ", NetFileStatus.Normal);

        List<NetFileDO> files = netFileDAO.selectByExample(selectExample);
        List<String> fileIds = dto.getFileIds();
        List<NetFileDO> changes = Lists.newArrayList();
        for (int i = 0; i < fileIds.size(); i++) {
            String id = fileIds.get(i);
            Optional<NetFileDO> o = files.stream().filter(f -> f.getId().equalsIgnoreCase(id)).findFirst();
            if (o.isPresent()) {
                NetFileDO file = o.get();
                //只更新变更顺序的
                if (!file.getParam4().equals(i)) {
                    file.setParam4(i);
                    changes.add(file);
                }
            }
        }

        for (NetFileDO f : changes) {
            f.setUpdateBy(dto.getAccountId());
            f.resetUpdateDate();
            netFileDAO.updateByPrimaryKey(f);
        }

        return ApiResult.success(null, null);
    }

    @Override
    public ApiResult uploadOrgAuthenticationAttach(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        if (!param.getParam().containsKey("type"))
            return ApiResult.failed("传递参数错误", null);

        if (param.isChunked())
            throw new UnsupportedOperationException("该上传不支持分片模式");

        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        FastdfsUploadResult fuResult = fastdfsService.uploadLogo(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);

        Integer type = new Integer((String) param.getParam().get("type")) ;
        //业务系统逻辑
        Example example = new Example(NetFileDO.class);
        example.createCriteria()
                .andCondition("company_id = ", companyId)
                .andCondition("type = ", type)
                .andCondition("status = ", NetFileStatus.Normal);
        List<NetFileDO> attach = netFileDAO.selectByExample(example);
        if (CollectionUtils.isEmpty(attach)) {
            saveNewNetFile(companyId, accountId, null, type, 0, null,null, fuResult);
        } else {
            NetFileDO netFileDO = attach.get(0);
            try {
                //删除原来的
                fastdfsService.delete(netFileDO.getFileGroup(), netFileDO.getFilePath());
            } catch (Exception ex) {
                log.error("FastDFS 删除文件发生异常（group:{} path:{}），{}", netFileDO.getFileGroup(), netFileDO.getFilePath(), ex.getMessage());
            }

            //更新新的Logo存储信息
            netFileDO.setFileGroup(fuResult.getFastdfsGroup());
            netFileDO.setFilePath(fuResult.getFastdfsPath());
            netFileDO.setFileSize(fuResult.getFileSize());
            netFileDO.setFileName(fuResult.getFileName());
            netFileDO.setFileExtName(fuResult.getFileExtName());
            netFileDO.setUpdateBy(accountId);
            netFileDO.setUpdateDate(LocalDateTime.now());
            netFileDAO.updateByPrimaryKey(netFileDO);
        }

        return ApiResult.success("文件已上传成功", fuResult);
    }


    /**
     * 上传报销附件
     */
    @Override
    public ApiResult uploadExpenseAttach(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String targetId = (String) param.getParam().get("targetId");
        if (StringUtils.isNullOrEmpty(targetId))
            return ApiResult.failed("报销单ID不能为空", null);
        return updateAttach(param,NetFileType.EXPENSE_ATTACH);
    }


    /**
     * 上传会议附件
     */
    @Override
    public ApiResult uploadScheduleAttach(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String targetId = (String) param.getParam().get("targetId");
        if (StringUtils.isNullOrEmpty(targetId))
            return ApiResult.failed("会议ID不能为空", null);
        return updateAttach(param,NetFileType.SCHEDULE_ATTACH);
    }

    @Override
    public ApiResult uploadCircleAttach(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String targetId = (String) param.getParam().get("targetId");
        if (StringUtils.isNullOrEmpty(targetId))
            return ApiResult.failed("讨论区ID不能为空", null);
       return updateAttach(param,NetFileType.CIRCLE_ATTACH);
    }

    @Override
    public ApiResult uploadNoticeAttach(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String targetId = (String) param.getParam().get("targetId");
        if (StringUtils.isNullOrEmpty(targetId))
            return ApiResult.failed("通知公告ID不能为空", null);
        return updateAttach(param,NetFileType.NOTICE_ATTACH);
    }

    private ApiResult updateAttach( MultipartFileParam param,Integer fileType) throws Exception{
        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String targetId = (String) param.getParam().get("targetId");
        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);
        //插入新记录
        ApiResult ar = saveNewNetFile(companyId, accountId, null, fileType, 0, targetId,null,fuResult);
        if (ar.isSuccessful()) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.OTHER, fuResult.getFileSize());
            return ApiResult.success(null,fuResult);
        }
        return ApiResult.failed(null, null);
    }

    private ApiResult updateAttachAndCrt( MultipartFileParam param,Integer fileType) throws Exception{
        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String targetId = (String) param.getParam().get("targetId");
        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        FastdfsUploadResult fuResult = fastdfsService.uploadImageAndCrtThumbImage(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);
        //插入新记录
        ApiResult ar = saveNewNetFileAndCrt(companyId, accountId, null,fileType, 0, targetId,null,fuResult);
        if (ar.isSuccessful()) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.OTHER, fuResult.getFileSize());
            return ApiResult.success(null,fuResult);
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 上传项目合同扫描件
     */
    @Override
    public ApiResult uploadProjectContract(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String projectId = (String) param.getParam().get("projectId");
        Boolean replacePrev = (Boolean) param.getParam().get("replacePrev");

        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        if (StringUtils.isNullOrEmpty(projectId))
            return ApiResult.failed("项目ID不能为空", null);

        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);

        /** 只在明确覆盖时删除之前的合同文件，保留多个文件 */
        if ((replacePrev != null) && (replacePrev == true)) {
            Example selectExample = new Example(NetFileDO.class);
            selectExample.createCriteria()
                    .andCondition("company_id = ", companyId)
                    .andCondition("project_id = ", projectId)
                    .andCondition("type = ", NetFileType.PROJECT_CONTRACT_ATTACH)
                    .andCondition("status = ", NetFileStatus.Normal)
                    .andCondition("file_name = ",fuResult.getFileName());

            List<NetFileDO> oldAttachs = netFileDAO.selectByExample(selectExample);
            if (oldAttachs != null && oldAttachs.size() > 0) {
                //累计扣减空间
                long subFileSize = 0L;
                for (NetFileDO oa : oldAttachs) {
                    oa.setStatus(NetFileStatus.Deleted.toString());
                    oa.setUpdateBy(accountId);
                    oa.setUpdateDate(LocalDateTime.now());
                    if (netFileDAO.updateByPrimaryKey(oa) > 0) {
                        subFileSize += oa.getFileSize();
                    }
                }
                //计算剩余空间
                if (subFileSize > 0L)
                    companyDiskService.recalcSizeOnFileRemoved(companyId, FileSizeSumType.OTHER, subFileSize);
            }
        }

        //插入新记录
        ApiResult ar = saveNewNetFile(companyId, accountId, projectId, NetFileType.PROJECT_CONTRACT_ATTACH, null, null,null, fuResult);
        if (ar.isSuccessful()) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.OTHER, fuResult.getFileSize());
            return ApiResult.success(null,fuResult);
        }

        return ApiResult.failed(null, null);
    }

    /**
     * 描述       上传收付款计划附件
     * 日期       2018/8/9
     *
     * @param request
     * @author 张成亮
     */
    @Override
    public FastdfsUploadResult uploadCostPlanAttach(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        return realUploadFile(param,NetFileType.FILE_TYPE_COST_PLAN_ATTACH);
    }

    //保存类型为type的文件到文档库中
    private FastdfsUploadResult realUploadFile(MultipartFileParam param, Integer fileType) throws Exception{
        TraceUtils.check(param != null);

        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String projectId = (String) param.getParam().get("projectId");
        String targetId = null;
        if(param.getParam().containsKey("targetId")){
            targetId = (String) param.getParam().get("targetId");
        }
        Boolean replacePrev = (Boolean) param.getParam().get("replacePrev");

        TraceUtils.check(StringUtils.isNotEmpty(companyId),"!组织ID不能为空");
        TraceUtils.check(StringUtils.isNotEmpty(accountId),"!账号ID不能为空");
        TraceUtils.check(StringUtils.isNotEmpty(projectId),"!项目ID不能为空");

        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow()) {
            return fuResult;
        }

        /** 只在明确覆盖时删除之前的文件，保留多个文件 */
        if ((replacePrev != null) && (replacePrev == true)) {
            Example selectExample = new Example(NetFileDO.class);
            selectExample.createCriteria()
                    .andCondition("company_id = ", companyId)
                    .andCondition("project_id = ", projectId)
                    .andCondition("type = ", fileType)
                    .andCondition("status = ", NetFileStatus.Normal)
                    .andCondition("file_name = ",fuResult.getFileName());

            List<NetFileDO> oldAttachs = netFileDAO.selectByExample(selectExample);
            if (oldAttachs != null && oldAttachs.size() > 0) {
                //累计扣减空间
                long subFileSize = 0L;
                for (NetFileDO oa : oldAttachs) {
                    oa.setStatus(NetFileStatus.Deleted.toString());
                    oa.setUpdateBy(accountId);
                    oa.setUpdateDate(LocalDateTime.now());
                    if (netFileDAO.updateByPrimaryKey(oa) > 0) {
                        subFileSize += oa.getFileSize();
                    }
                }
                //计算剩余空间
                if (subFileSize > 0L)
                    companyDiskService.recalcSizeOnFileRemoved(companyId, FileSizeSumType.OTHER, subFileSize);
            }
        }

        //插入新记录
        ApiResult ar = saveNewNetFile(companyId, accountId, projectId, NetFileType.PROJECT_CONTRACT_ATTACH, null, targetId,null, fuResult);
        if (ar.isSuccessful()) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.OTHER, fuResult.getFileSize());
            return fuResult;
        }

        return null;
    }

    @Override
    public ApiResult uploadProjectContractForApp(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);

        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String projectId = (String) param.getParam().get("projectId");

        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);

        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);

        if (StringUtils.isNullOrEmpty(projectId))
            return ApiResult.failed("项目ID不能为空", null);

        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);

        //插入新记录  projectId 暂时设置为 null 等前端 保存的时候，再设置 projectId，并且处理剩余空间
        ApiResult ar = saveNewNetFile(companyId, accountId, null, NetFileType.PROJECT_CONTRACT_ATTACH, null, null,null, fuResult);
        if (ar.isSuccessful()) {
            return ApiResult.success(null,fuResult);
        }
        return ApiResult.failed(null, null);
    }

    @Override
    public ApiResult uploadGroupImg(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String orgId = (String) param.getParam().get("orgId");
        if (StringUtils.isNullOrEmpty(orgId))
            return ApiResult.failed("群组orgId不能为空", null);

        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);

        //更新群组LOGO
        GroupImgUpdateDTO imgUpdateDTO = new GroupImgUpdateDTO();
        imgUpdateDTO.setOrgId(orgId);
        imgUpdateDTO.setGroupType(ImGroupType.CUSTOM);
        imgUpdateDTO.setImg(fuResult.getFastdfsGroup() + "/" + fuResult.getFastdfsPath());
        imGroupDAO.updateGroupImg(imgUpdateDTO);

        return ApiResult.success("文件已上传成功", fuResult);

    }

    @Override
    public ApiResult createPersonalDir(DirectoryDTO dir) throws Exception {
        dir.setType(NetFileType.DIRECTORY_PERSONALLY);
        if(StringUtils.isNullOrEmpty(dir.getFileName())){
            return ApiResult.failed("文件夹名称不能为空", null);
        }
        if(!CollectionUtils.isEmpty(netFileDAO.getNetFileOrDir(dir))){
            return ApiResult.failed("同级目录下已存在名称重复的项", null);
        }
        return saveNewNetFile(dir.getCompanyId(),dir.getAccountId(),null,dir.getType(),0,null,dir.getPid(),null);
    }

    @Override
    public ApiResult uploadPersonalFile(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileParam.parse(request);
        String companyId = (String) param.getParam().get("companyId");
        String accountId = (String) param.getParam().get("accountId");
        String pid = (String) param.getParam().get("pid");
        String fileName =param.getFileName();
        if (StringUtils.isNullOrEmpty(companyId))
            return ApiResult.failed("组织ID不能为空", null);
        if (StringUtils.isNullOrEmpty(accountId))
            return ApiResult.failed("账号ID不能为空", null);
        if (StringUtils.isNullOrEmpty(pid))
            return ApiResult.failed("上传目录不能为空", null);
        if (StringUtils.isNullOrEmpty(fileName))
            return ApiResult.failed("文件名不能为空", null);
        DirectoryDTO dir = new DirectoryDTO();
        dir.setAccountId(accountId);
        dir.setCompanyId(companyId);
        dir.setFileName(fileName);
        dir.setPid(pid);
        dir.setType(NetFileType.DIRECTORY_PERSONALLY);
        if(!CollectionUtils.isEmpty(netFileDAO.getNetFileOrDir(dir))){
            return ApiResult.failed("同级目录下已存在名称重复的项", null);
        }
        FastdfsUploadResult fuResult = fastdfsService.upload(param);
        if (fuResult.getNeedFlow())
            return ApiResult.success(null, fuResult);
        //插入新记录
        ApiResult ar = saveNewNetFile(companyId, accountId, null, NetFileType.NOTICE_ATTACH, 0, null, null,fuResult);
        if (ar.isSuccessful()) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileAdded(companyId, FileSizeSumType.OTHER, fuResult.getFileSize());
            return ApiResult.success(null,fuResult);
        }
        return ApiResult.failed(null, null);
    }


    /**
     * 删除附件
     */
    @Override
    public ApiResult delete(DeleteDTO dto) {
        if (StringUtils.isNullOrEmpty(dto.getId()))
            return ApiResult.failed("ID不能为空", null);

        NetFileDO netFileDO = netFileDAO.selectByPrimaryKey(dto.getId());
        if (netFileDO == null)
            return ApiResult.failed("找不到要删除的项", null);

        //TODO  是逻辑删除，需要定时任务清理
        NetFileDO updateObj = new NetFileDO();
        updateObj.setId(dto.getId());
        updateObj.setStatus(NetFileStatus.Deleted.toString());
        updateObj.setUpdateBy(dto.getAccountId());
        updateObj.setUpdateDate(LocalDateTime.now());

        if (netFileDAO.updateByPrimaryKeySelective(updateObj) > 0) {
            //计算剩余空间
            companyDiskService.recalcSizeOnFileRemoved(netFileDO.getCompanyId(),FileSizeSumType.OTHER, netFileDO.getFileSize());
            return ApiResult.success(null, null);
        }
        return ApiResult.failed(null, null);
    }

    /**
     * app端合同附件上传后，点击保存，处理文件对应的记录
     */
    @Override
    public ApiResult handleProjectContract(NetFileDTO dto) throws Exception {
        List<NetFileDO> addList = null;
        List<NetFileDO> deleteList = null;
        //业务系统逻辑
        if(!CollectionUtils.isEmpty(dto.getUploadFileList())){//处理新上传的记录
            dto.setIds(dto.getUploadFileList());
            addList = netFileDAO.getNetFileByIds(dto);
            long fileSize = 0L;
            for(NetFileDO file:addList){
                file.setProjectId(dto.getId());
                netFileDAO.updateByPrimaryKey(file);
                fileSize += file.getFileSize();
            }
            companyDiskService.recalcSizeOnFileAdded(dto.getCompanyId(), FileSizeSumType.OTHER, fileSize);
        }
        if(!CollectionUtils.isEmpty(dto.getDeleteFileList())){//处理被删除的记录
            dto.setIds(dto.getDeleteFileList());
            deleteList = netFileDAO.getNetFileByIds(dto);
            long fileSize = 0L;
            for(NetFileDO file:deleteList){
                fileSize += file.getFileSize();
                file.setStatus(NetFileStatus.Deleted.toString());
                file.setUpdateBy(dto.getAccountId());
                file.setUpdateDate(LocalDateTime.now());
                netFileDAO.updateByPrimaryKey(file);
            }
            companyDiskService.recalcSizeOnFileRemoved(dto.getCompanyId(), FileSizeSumType.OTHER, fileSize);
        }
        return ApiResult.success(null, null);
    }


    /**
     * 插入新的NetFile纪录
     */
    private ApiResult saveNewNetFile(String companyId, String accountId, String projectId, Integer netFileType, Integer seq, String targetId, String pid,FastdfsUploadResult fuResult) {
        NetFileDO netFileDO = saveNetFile(companyId,accountId,projectId,netFileType,seq,targetId,pid,fuResult);
        if (netFileDAO.insert(netFileDO) > 0) {
            if(fuResult!=null){
                fuResult.setNetFileId(netFileDO.getId());
                fuResult.setNetFileSeq(seq);
                fuResult.setFileFullPath(FdfsWebServer.webServerUrl+fuResult.getFastdfsGroup()+"/"+fuResult.getFastdfsPath());
            }
            return ApiResult.success(null, netFileDO);
        }
        return ApiResult.failed(null, null);
    }

    /**
     * 插入新的NetFile纪录（包含缩略图）
     */
    private ApiResult saveNewNetFileAndCrt(String companyId, String accountId, String projectId, Integer netFileType, Integer seq, String targetId, String pid,FastdfsUploadResult fuResult) {
        NetFileDO netFileDO = saveNetFile(companyId,accountId,projectId,netFileType,seq,targetId,pid,fuResult);
        if (netFileDAO.insert(netFileDO) > 0) {
            //插入缩略图记录
            NetFileCrtDO crtDO = netFileCrtDAO.selectByPrimaryKey(netFileDO.getId());
            if(crtDO==null){
                crtDO = new NetFileCrtDO();
                crtDO.setId(netFileDO.getId());
                crtDO.setDeleted(0);
                crtDO.setCrtFilePath(this.fastdfsService.getCrtPath(netFileDO.getFilePath()));
                netFileCrtDAO.insert(crtDO);
            }else {
                crtDO.setId(netFileDO.getId());
                crtDO.setDeleted(0);
                crtDO.setCrtFilePath(this.fastdfsService.getCrtPath(netFileDO.getFilePath()));
                netFileCrtDAO.updateByPrimaryKeySelective(crtDO);
            }
            if(fuResult!=null){
                fuResult.setNetFileId(netFileDO.getId());
                fuResult.setNetFileSeq(seq);
                fuResult.setFileFullPath(FdfsWebServer.webServerUrl+fuResult.getFastdfsGroup()+"/"+fuResult.getFastdfsPath());
            }
            return ApiResult.success(null, netFileDO);
        }
        return ApiResult.failed(null, null);
    }

    private NetFileDO saveNetFile(String companyId, String accountId, String projectId, Integer netFileType, Integer seq, String targetId, String pid,FastdfsUploadResult fuResult) {
        NetFileDO netFileDO = new NetFileDO();
        netFileDO.initEntity();
        netFileDO.setCreateBy(accountId);
        netFileDO.setUpdateBy(accountId);
        netFileDO.setCompanyId(companyId);
        netFileDO.setProjectId(projectId);
        netFileDO.setStatus(NetFileStatus.Normal.toString());
        netFileDO.setType(netFileType);
        netFileDO.setIsCustomize(0);
        netFileDO.setPid(pid);
        netFileDO.setSkyDrivePath(netFileDO.getId());//暂时没有设置
        if(pid!=null){
            NetFileDO net = netFileDAO.selectByPrimaryKey(pid);
            if(net!=null){
                netFileDO.setSkyDrivePath(net.getSkyDrivePath()+"-"+netFileDO.getId());
            }
        }
        if(fuResult!=null){
            netFileDO.setFileGroup(fuResult.getFastdfsGroup());
            netFileDO.setFilePath(fuResult.getFastdfsPath());
            netFileDO.setFileSize(fuResult.getFileSize());
            netFileDO.setFileName(fuResult.getFileName());
            netFileDO.setFileExtName(fuResult.getFileExtName());
        }
        netFileDO.setParam4(seq);
        netFileDO.setTargetId(targetId);
        return netFileDO;
    }
}
