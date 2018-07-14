package com.maoding.filecenter.module.file;

import com.maoding.core.bean.ApiResult;
import com.maoding.core.exception.DataNotFoundException;
import com.maoding.filecenter.module.file.dto.DeleteDTO;
import com.maoding.filecenter.module.file.dto.DirectoryDTO;
import com.maoding.filecenter.module.file.dto.NetFileRenameDTO;
import com.maoding.filecenter.module.file.service.NetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wuwq on 2017/05/27.
 */
@RestController
@RequestMapping("/fileCenter/netFile")
public class NetFileController {

    @Autowired
    private NetFileService netFileService;

    /**
     * 创建目录
     */
    @RequestMapping(value = "/createDirectory", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult createDirectory(@RequestBody DirectoryDTO dir) throws DataNotFoundException {
        return netFileService.createDirectory(dir);
    }

    /**
     * 上传文件
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadFile(HttpServletRequest request) throws Exception {
        return netFileService.uploadFile(request);
    }

    /**
     * 重命名文件
     */
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult renameFile(@RequestBody NetFileRenameDTO dto) {
        return netFileService.rename(dto);
    }

    /**
     * 删除文件
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult deleteFile(@RequestBody DeleteDTO dto) {
        return netFileService.delete(dto);
    }
}
