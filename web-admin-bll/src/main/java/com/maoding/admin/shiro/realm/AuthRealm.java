package com.maoding.admin.shiro.realm;

import com.maoding.admin.module.account.dto.AccountDTO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Wuwq on 2017/07/17.
 */
public class AuthRealm extends AuthorizingRealm {

    private List<AccountDTO> accountDB = Arrays.asList(
            new AccountDTO("罗娉婷", "13798291624"),
            new AccountDTO("谢绍灿", "18038026646"),
            new AccountDTO("张佳辉", "13652381815"),
            new AccountDTO("黄铎", "13510106915"),
            new AccountDTO("毛双凤", "18665965065"),
            new AccountDTO("郭志彬", "15013551861"),
            new AccountDTO("王汝彬", "18620154157"),
            new AccountDTO("陶均明", "15112308656"),
            new AccountDTO("许佳迪", "15353032981"),
            new AccountDTO("叶文龙", "15119577622"),
            new AccountDTO("温盛健", "18620251394"),
            new AccountDTO("谢凯", "13926517031"),
            new AccountDTO("练思云", "15606767157"),
            new AccountDTO("汪威", "18871465314"),
            new AccountDTO("蔡晓钿", "18824303231"),
            new AccountDTO("卢沂", "13926516981"),
            new AccountDTO("吴伟强", "13602423201"),
            new AccountDTO("闵康", "15527140009"),
            new AccountDTO("张成亮", "13680809727")
    );

    /*认证*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken authToken = (UsernamePasswordToken) authenticationToken;

        //暂时写死
        String account = (String) authToken.getPrincipal();
        String password = String.valueOf(authToken.getPassword());

        List<AccountDTO> accountDTOS = accountDB.stream().filter(c -> c.getAccount().equalsIgnoreCase(account)).collect(Collectors.toList());
        if (accountDTOS.size() > 0 && password.equalsIgnoreCase(account)) {
            return new SimpleAuthenticationInfo(accountDTOS.get(0), password, getName());
        } else {
            throw new AuthenticationException("token认证失败");
        }
    }

    /*授权*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        return authorizationInfo;
    }
}
