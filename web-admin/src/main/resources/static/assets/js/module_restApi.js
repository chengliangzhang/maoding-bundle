/**
 * Created by Wuwq on 2017/2/27.
 */
var restApi = {
    /** 登陆 **/
    url_loginSubmit: window.rootPath + '/account/login',

    /** 企业认证 **/
    url_getAuthenticationPage: window.rootPath + '/orgAuth/getAuthenticationPage',
    url_authorizeAuthentication: window.rootPath + '/orgAuth/authorizeAuthentication',

    /** 历史数据导入 **/
    url_historyData_importProjects: window.rootPath + '/historyData/importProjects',


    /** 菜单配置 **/
    url_configuration_saveView: window.rootPath + '/configuration/saveView',
    url_configuration_getParentViewList: window.rootPath + '/configuration/getParentViewList',
    url_configuration_getViewList: window.rootPath + '/configuration/getViewList',
    url_configuration_saveOperatePermission: window.rootPath + '/configuration/saveOperatePermission',
    url_configuration_getRoleForProject: window.rootPath + '/configuration/getRoleForProject',
    url_configuration_getAllViewByRoleCode: window.rootPath + '/configuration/getAllViewByRoleCode',
    url_configuration_saveRolePermission: window.rootPath + '/configuration/saveRolePermission',
    url_rule_getRuleType: window.rootPath + '/rule/getRuleType',
    url_rule_getCandidateType: window.rootPath + '/rule/getCandidateType',
    url_rule_saveRule: window.rootPath + '/rule/saveRule'

};