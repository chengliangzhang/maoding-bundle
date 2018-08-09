package com.maoding.filecenter.module.file.service;

import com.maoding.core.bean.ApiResult;
import com.maoding.core.bean.FastdfsUploadResult;
import com.maoding.filecenter.module.file.dto.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Wuwq on 2017/05/27.
 */
public interface AttachmentService {

    /**
     * 保存公司logo文件（仅Web端）
     */
    ApiResult saveCompanyLogo(SaveCompanyLogoDTO dto);

    /**
     * 上传公司logo图片
     */
    ApiResult uploadCompanyLogo(HttpServletRequest request) throws Exception;


    /**
     * 上传公司轮播图片
     */
    ApiResult uploadCompanyBanner(HttpServletRequest request) throws Exception;

    /**
     * 调整公司轮播图片顺序
     */
    ApiResult orderCompanyBanner(NetFileOrderDTO dto) throws Exception;


    /**
     *上传组织认证附件
     */
    ApiResult uploadOrgAuthenticationAttach(HttpServletRequest request) throws Exception;

    /**
     * 上传报销附件
     */
    ApiResult uploadExpenseAttach(HttpServletRequest request) throws Exception;

    /**
     * 上传会议附件
     */
    ApiResult uploadScheduleAttach(HttpServletRequest request) throws Exception;

    /**
     * 上传讨论组图片
     */
    ApiResult uploadCircleAttach(HttpServletRequest request) throws Exception;

    /**
     * 上传通知公告附件
     */
    ApiResult uploadNoticeAttach(HttpServletRequest request) throws Exception;

    /**
     * 上传项目合同扫描件
     */
    ApiResult uploadProjectContract(HttpServletRequest request) throws Exception;

    /**
     * 描述       上传收付款计划附件
     * 日期       2018/8/9
     * @author   张成亮
     **/
    FastdfsUploadResult uploadCostPlanAttach(HttpServletRequest request) throws Exception;

    /**
     * 上传项目合同扫描件
     */
    ApiResult uploadProjectContractForApp(HttpServletRequest request) throws Exception;
    /**
     * 上传自定义群组头像
     */
    ApiResult uploadGroupImg(HttpServletRequest request) throws Exception;

    /**
     * 个人创建目录
     */
    ApiResult createPersonalDir(DirectoryDTO dir) throws Exception;

    /**
     * 个人上传文件
     */
    ApiResult uploadPersonalFile(HttpServletRequest request) throws Exception;

    /**
     * 删除附件
     */
    ApiResult delete(DeleteDTO dto);

    /**
     * app端合同附件上传后，点击保存，处理文件对应的记录
     */
    ApiResult handleProjectContract(NetFileDTO dto) throws Exception;
}
