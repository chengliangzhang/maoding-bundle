package com.maoding.filecenter.module.file;

import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.filecenter.module.file.dto.*;
import com.maoding.filecenter.module.file.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wuwq on 2017/05/27.
 */
@RestController
@RequestMapping("/fileCenter/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    /**
     * 描述       上传收付款计划附件
     * 日期       2018/8/9
     * @author   张成亮
     **/
    @RequestMapping(value = "/uploadCostPlanAttach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadCostPlanAttach(HttpServletRequest request) throws Exception {
        FastdfsUploadResult result = attachmentService.uploadCostPlanAttach(request);
        if (request != null){
            return ApiResult.success("上传成功",result);
        } else {
            return ApiResult.failed("上传失败");
        }
    }


    /**
     * 上传项目合同扫描件
     */
    @RequestMapping(value = "/uploadProjectContract", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadProjectContract(HttpServletRequest request) throws Exception {
        return attachmentService.uploadProjectContract(request);
    }

    /**
     * 上传项目合同扫描件
     */
    @RequestMapping(value = "/uploadProjectContractForApp", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadProjectContractForApp(HttpServletRequest request) throws Exception {
        return attachmentService.uploadProjectContractForApp(request);
    }

    /**
     * 上传项目合同扫描件
     */
    @RequestMapping(value = "/handleProjectContract", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult handleProjectContract(@RequestBody NetFileDTO dto) throws Exception {
        return attachmentService.handleProjectContract(dto);
    }

    /**
     * 上传公司logo
     */
    @RequestMapping(value = "/uploadCompanyLogo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadCompanyLogo(HttpServletRequest request) throws Exception {
        return attachmentService.uploadCompanyLogo(request);
    }

    /**
     * 保存公司logo
     */
    @RequestMapping(value = "/saveCompanyLogo", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult saveCompanyLogo(@RequestBody SaveCompanyLogoDTO dto) throws Exception {
        return attachmentService.saveCompanyLogo(dto);
    }

    /**
     * 上传公司轮播图
     */
    @RequestMapping(value = "/uploadCompanyBanner", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadCompanyBanner(HttpServletRequest request) throws Exception {
        return attachmentService.uploadCompanyBanner(request);
    }

    /**
     * 上传组织认证附件
     */
    @RequestMapping(value = "/uploadOrgAuthenticationAttach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadOrgAuthenticationAttach(HttpServletRequest request) throws Exception {
        return attachmentService.uploadOrgAuthenticationAttach(request);
    }

    /**
     * 调整公司轮播图片顺序
     */
    @RequestMapping(value = "/orderCompanyBanner", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult orderCompanyBanner(@RequestBody NetFileOrderDTO dto) throws Exception {
        return attachmentService.orderCompanyBanner(dto);
    }

    /**
     * 上传报销附件
     */
    @RequestMapping(value = "/uploadExpenseAttach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadExpenseAttach(HttpServletRequest request) throws Exception {
        return attachmentService.uploadExpenseAttach(request);
    }

    /**
     * 上传会议附件
     */
    @RequestMapping(value = "/uploadScheduleAttach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadScheduleAttach(HttpServletRequest request) throws Exception {
        return attachmentService.uploadScheduleAttach(request);
    }

    /**
     * 上传通知公告附件
     */
    @RequestMapping(value = "/uploadNoticeAttach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadNoticeAttach(HttpServletRequest request) throws Exception {
        return attachmentService.uploadNoticeAttach(request);
    }

    /**
     * 上传项目讨论组附近
     */
    @RequestMapping(value = "/uploadCircleAttach", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadCircleAttach(HttpServletRequest request) throws Exception {
        return attachmentService.uploadCircleAttach(request);
    }

    /**
     * 上传群组（自定义）头像
     */
    @RequestMapping(value = "/uploadGroupImg", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadGroupImg(HttpServletRequest request) throws Exception {
        return attachmentService.uploadGroupImg(request);
    }



    /**
     * 个人自定义文件夹
     */
    @RequestMapping(value = "/createPersonalDir", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult createPersonalDir(@RequestBody DirectoryDTO dir) throws Exception {
        return attachmentService.createPersonalDir(dir);
    }

    /**
     * 个人上传文件
     */
    @RequestMapping(value = "/uploadPersonalFile", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult uploadPersonalFile(HttpServletRequest request) throws Exception {
        return attachmentService.uploadPersonalFile(request);
    }


    /**
     * 删除文件
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult deleteFile(@RequestBody DeleteDTO dto) {
        return attachmentService.delete(dto);
    }
}
