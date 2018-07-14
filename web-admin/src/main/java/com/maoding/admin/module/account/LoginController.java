package com.maoding.admin.module.account;

import com.maoding.admin.module.account.dto.AccountDTO;
import com.maoding.admin.module.account.dto.AccountLoginDTO;
import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Wuwq on 2017/07/18.
 */

@Controller
@RequestMapping("/account")
public class LoginController extends BaseController {


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/";
        }
        return "account/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult loginSubmit(@RequestBody AccountLoginDTO dto) {
        UsernamePasswordToken token = new UsernamePasswordToken(dto.getAccount(), dto.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            if (subject.isAuthenticated()) {
                AccountDTO account = ((AccountDTO) subject.getPrincipal());
                subject.getSession().setAttribute("accountName", account.getName());
                return ApiResult.success(null, "/");
            }
        } catch (AuthenticationException e) {

        }
        return ApiResult.failed("账号或密码错误");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "account/login";
    }
}
