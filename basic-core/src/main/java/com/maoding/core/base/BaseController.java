package com.maoding.core.base;

import com.maoding.utils.StringUtils;
import com.maoding.utils.TraceUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.validation.constraints.NotNull;

public class BaseController {
    
    /**
     * 描述       补充当前用户信息
     * 日期       2018/9/12
     * @author   张成亮
     **/
    protected void updateCurrentUserInfo(@NotNull BaseDTO editDTO){
        if (StringUtils.isEmpty(editDTO.getCurrentCompanyId())){
            editDTO.setCurrentCompanyId(getFromSession("companyId",String.class));
        }
        if (StringUtils.isEmpty(editDTO.getAccountId())){
            editDTO.setAccountId(getFromSession("userId",String.class));
        }
        if (StringUtils.isEmpty(editDTO.getCurrentCompanyUserId())){
            editDTO.setCurrentCompanyUserId(getFromSession("companyUserId",String.class));
        }
    }

    /**
     * 描述       保存信息到当前会话中
     * 日期       2018/9/12
     * @author   张成亮
     **/
    protected <T> void setToSession(@NotNull String key,@NotNull T code){
        Subject subject = SecurityUtils.getSubject();
        if (subject != null){
            Session session = subject.getSession();
            if (session != null){
                session.setAttribute(key,code);
            }
        }
    }

    /**
     * 描述       从当前会话中读取信息
     * 日期       2018/9/12
     * @author   张成亮
     **/
    protected <T> T getFromSession(@NotNull String key, @NotNull Class<T> clazz){
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Session session = subject.getSession();
                if (session != null) {
                    return clazz.cast(session.getAttribute(key));
                }
            }
        } catch (UnavailableSecurityManagerException e) {
            TraceUtils.error("无法读取会话信息",e);
        }
        return null;
    }

}
