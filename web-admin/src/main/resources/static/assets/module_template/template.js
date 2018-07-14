/*TMODJS:{"version":"1.0.0"}*/
!function () {

    function template (filename, content) {
        return (
            /string|function/.test(typeof content)
            ? compile : renderFile
        )(filename, content);
    };


    var cache = template.cache = {};
    var String = this.String;

    function toString (value, type) {

        if (typeof value !== 'string') {

            type = typeof value;
            if (type === 'number') {
                value += '';
            } else if (type === 'function') {
                value = toString(value.call(value));
            } else {
                value = '';
            }
        }

        return value;

    };


    var escapeMap = {
        "<": "&#60;",
        ">": "&#62;",
        '"': "&#34;",
        "'": "&#39;",
        "&": "&#38;"
    };


    function escapeFn (s) {
        return escapeMap[s];
    }


    function escapeHTML (content) {
        return toString(content)
        .replace(/&(?![\w#]+;)|[<>"']/g, escapeFn);
    };


    var isArray = Array.isArray || function(obj) {
        return ({}).toString.call(obj) === '[object Array]';
    };


    function each (data, callback) {
        if (isArray(data)) {
            for (var i = 0, len = data.length; i < len; i++) {
                callback.call(data, data[i], i, data);
            }
        } else {
            for (i in data) {
                callback.call(data, data[i], i);
            }
        }
    };


    function resolve (from, to) {
        var DOUBLE_DOT_RE = /(\/)[^/]+\1\.\.\1/;
        var dirname = ('./' + from).replace(/[^/]+$/, "");
        var filename = dirname + to;
        filename = filename.replace(/\/\.\//g, "/");
        while (filename.match(DOUBLE_DOT_RE)) {
            filename = filename.replace(DOUBLE_DOT_RE, "/");
        }
        return filename;
    };


    var utils = template.utils = {

        $helpers: {},

        $include: function (filename, data, from) {
            filename = resolve(from, filename);
            return renderFile(filename, data);
        },

        $string: toString,

        $escape: escapeHTML,

        $each: each
        
    };


    var helpers = template.helpers = utils.$helpers;


    function renderFile (filename, data) {
        var fn = template.get(filename) || showDebugInfo({
            filename: filename,
            name: 'Render Error',
            message: 'Template not found'
        });
        return data ? fn(data) : fn; 
    };


    function compile (filename, fn) {

        if (typeof fn === 'string') {
            var string = fn;
            fn = function () {
                return new String(string);
            };
        }

        var render = cache[filename] = function (data) {
            try {
                return new fn(data, filename) + '';
            } catch (e) {
                return showDebugInfo(e)();
            }
        };

        render.prototype = fn.prototype = utils;
        render.toString = function () {
            return fn + '';
        };

        return render;
    };


    function showDebugInfo (e) {

        var type = "{Template Error}";
        var message = e.stack || '';

        if (message) {
            // 利用报错堆栈信息
            message = message.split('\n').slice(0,2).join('\n');
        } else {
            // 调试版本，直接给出模板语句行
            for (var name in e) {
                message += "<" + name + ">\n" + e[name] + "\n\n";
            }  
        }

        return function () {
            if (typeof console === "object") {
                console.error(type + "\n\n" + message);
            }
            return type;
        };
    };


    template.get = function (filename) {
        return cache[filename.replace(/^\.\//, '')];
    };


    template.helper = function (name, helper) {
        helpers[name] = helper;
    };


    if (typeof define === 'function') {define(function() {return template;});} else if (typeof exports !== 'undefined') {module.exports = template;} else {this.template = template;}
    /*自动组合rootPath生成完整URL*/
template.helper('_url', function (url) {
    return window.rootPath + url;
});

/*判断字符串是否为undefined、Null或空*/
template.helper('_isNullOrBlank', function (str) {
    return str === void 0 || str === null || _.isBlank(str);
});

template.helper('_formatFileSize', function (fileSize) {
    var temp;
    if (fileSize === void 0 || fileSize === null)
        return '';
    else if (fileSize < 1024) {
        return fileSize + 'B';
    } else if (fileSize < (1024 * 1024)) {
        temp = parseFloat(fileSize / 1024).toFixed(3);
        return temp.substring(0, temp.length - 1) + 'KB';
    } else if (fileSize < (1024 * 1024 * 1024)) {
        temp = parseFloat(fileSize / (1024 * 1024)).toFixed(3);
        return temp.substring(0, temp.length - 1) + 'MB';
    } else {
        temp = parseFloat(fileSize / (1024 * 1024 * 1024)).toFixed(3);
        return temp.substring(0, temp.length - 1) + 'GB';
    }
});


template.helper('_isNowDiffTargetDateNDaysMore', function (targetDate, n) {
    var diff = moment().diff(moment(targetDate), 'days') + 1;
    return diff >= n;
});
    /*v:1*/
template('m_common/m_popover',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,popoverStyle=$data.popoverStyle,$string=$utils.$string,titleHtml=$data.titleHtml,contentStyle=$data.contentStyle,content=$data.content,$out='';$out+='<div class="popover m-popover box-shadow" role="tooltip" style="';
$out+=$escape(popoverStyle);
$out+='"> <div class="arrow" style="left: 50%;"></div>  ';
$out+=$string(titleHtml);
$out+=' <div class="popover-content" style="';
$out+=$escape(contentStyle);
$out+='"> ';
$out+=$string(content);
$out+=' </div> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_popover_confirm',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$string=$utils.$string,confirmMsg=$data.confirmMsg,$out='';$out+='<div> <p class="f-s-13">';
$out+=$string(confirmMsg);
$out+='</p> <p class="pull-right" > <button type="button" class="popover-btn-no btn btn-default btn-xs m-popover-close" style="line-height:22px;">取消 </button> <button type="button" class="popover-btn-yes btn btn-success btn-xs m-popover-submit" style="line-height:22px;">确定</button> </p> </div>';
return new String($out);
});/*v:1*/
template('m_common/m_sidebar',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,_url=$helpers._url,$out='';$out+='  <div class="page-sidebar navbar-collapse collapse">        <ul class="page-sidebar-menu" data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">  <li class="heading"> <h3 class="uppercase">企业认证</h3> </li> <li class="nav-item"> <a href="';
$out+=$escape(_url('/orgAuth/approveList'));
$out+='" class="nav-link" data-nav-id="orgAuth-approveList"> <i class="icon-briefcase"></i> <span class="title">认证审核</span> <span class="selected"></span> </a> </li> <li class="heading"> <h3 class="uppercase">历史数据</h3> </li> <li class="nav-item"> <a href="';
$out+=$escape(_url('/historyData/entry'));
$out+='" class="nav-link" data-nav-id="historyData-entry"> <i class="icon-briefcase"></i> <span class="title">导入</span> </a> </li> <li class="heading"> <h3 class="uppercase">后台配置</h3> </li> <li class="nav-item"> <a href="';
$out+=$escape(_url('/configuration/menuSet'));
$out+='" class="nav-link" data-nav-id="configuration-menuSet"> <i class="icon-briefcase"></i> <span class="title">菜单配置</span> </a> </li> <li class="nav-item"> <a href="';
$out+=$escape(_url('/configuration/roleViews'));
$out+='" class="nav-link" data-nav-id="configuration-roleViews"> <i class="icon-briefcase"></i> <span class="title">角色菜单配置</span> </a> </li> <li class="nav-item"> <a href="';
$out+=$escape(_url('/configuration/rulesSet'));
$out+='" class="nav-link" data-nav-id="configuration-rulesSet"> <i class="icon-briefcase"></i> <span class="title">规则配置</span> </a> </li>  </ul> </div>';
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_menu_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,parentViewList=$data.parentViewList,$each=$utils.$each,p=$data.p,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<form class="form-horizontal m_configuration_menu_add" role="form"> <div class="form-body"> <div class="form-group"> <label class="col-md-2 control-label">菜单名：</label> <div class="col-md-9"> <input class="form-control" name="name" /> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">父菜单：</label> <div class="col-md-9"> <select name="pid"> <option value="">请选择父菜单</option> ';
if(parentViewList!=null && parentViewList.length>0){
$out+=' ';
$each(parentViewList,function(p,$index){
$out+=' <option value="';
$out+=$escape(p.id);
$out+='">';
$out+=$escape(p.viewName);
$out+='</option> ';
});
$out+=' ';
}
$out+=' </select> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">URL地址：</label> <div class="col-md-9"> <input class="form-control" name="url" /> </div> </div> <div class="form-group"> <div class="col-md-9 col-md-offset-2"> <button class="btn btn-default" data-action="save">保存</button> <button class="btn btn-default" data-action="back">返回</button> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_menu_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,viewList=$data.viewList,$each=$utils.$each,v=$data.v,$index=$data.$index,$escape=$utils.$escape,o=$data.o,c=$data.c,ci=$data.ci,$out='';$out+='<table class="table table-striped table-bordered table-hover"> <thead> <tr> <th>父模块</th> <th>子模块</th> <th>操作</th> </tr> </thead> <tbody class="m-page-data"> ';
if(viewList!=null && viewList.length>0){
$out+=' ';
$each(viewList,function(v,$index){
$out+=' <tr> <td rowspan="';
$out+=$escape(v.childList.length>0?v.childList.length:'');
$out+='">';
$out+=$escape(v.viewName);
$out+='</td> <td> ';
if(v.childList.length>0){
$out+=' ';
$out+=$escape(v.childList[0].viewName);
$out+=' <a class="btn btn-xs btn-default" data-action="addOperation" data-id="';
$out+=$escape(v.childList[0].id);
$out+='" data-code="';
$out+=$escape(v.childList[0].viewCode);
$out+='" data-name="';
$out+=$escape(v.childList[0].viewName);
$out+='" >添加操作</a> ';
}
$out+=' </td> <td> ';
if(v.childList.length>0 && v.childList[0].operateList.length>0){
$out+=' ';
$each(v.childList[0].operateList,function(o,$index){
$out+=' ';
$out+=$escape(o.operateName);
$out+=', ';
});
$out+=' ';
}
$out+=' </td> </tr> ';
if(v.childList.length>1 ){
$out+=' ';
$each(v.childList,function(c,ci){
$out+=' ';
if(ci>0){
$out+=' <tr> <td> ';
$out+=$escape(c.viewName);
$out+=' <a class="btn btn-xs btn-default" data-action="addOperation" data-id="';
$out+=$escape(c.id);
$out+='" data-code="';
$out+=$escape(c.viewCode);
$out+='" data-name="';
$out+=$escape(c.viewName);
$out+='" >添加操作</a> </td> <td> ';
if(c.operateList.length>0){
$out+=' ';
$each(c.operateList,function(o,$index){
$out+=' ';
$out+=$escape(o.operateName);
$out+=', ';
});
$out+=' ';
}
$out+=' </td> </tr> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_operation_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,belongingView=$data.belongingView,$out='';$out+='<form class="form-horizontal m_configuration_operation_add" role="form"> <div class="form-body"> <div class="form-group"> <label class="col-md-2 control-label">操作名：</label> <div class="col-md-9"> <input class="form-control" name="name" /> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">所属菜单：</label> <div class="col-md-9"> ';
$out+=$escape(belongingView.viewName);
$out+=' </div> </div> <div class="form-group"> <label class="col-md-2 control-label">操作类型：</label> <div class="col-md-9"> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">组织权限类型：</label> <div class="col-md-9"> </div> </div> <div class="form-group"> <div class="col-md-9 col-md-offset-2"> <button class="btn btn-default" data-action="save">保存</button> <button class="btn btn-default" data-action="back">返回</button> </div> </div> </div> </form>';
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_role',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,roleList=$data.roleList,$each=$utils.$each,r=$data.r,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<div class="list-group"> ';
if(roleList!=null && roleList.length>0){
$out+=' ';
$each(roleList,function(r,$index){
$out+=' <a href="javascript:void(0);" class="list-group-item" data-action="getViewsByRole" data-code="';
$out+=$escape(r.roleCode);
$out+='" data-id="';
$out+=$escape(r.id);
$out+='" data-type="';
$out+=$escape(r.roleType);
$out+='"> ';
$out+=$escape(r.name);
$out+=' </a> ';
});
$out+=' ';
}
$out+=' </div> ';
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_rules_add',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,ruleList=$data.ruleList,$each=$utils.$each,p=$data.p,$index=$data.$index,$escape=$utils.$escape,$out='';$out+='<form class="form-horizontal m_configuration_rules_add" role="form"> <div class="form-body"> <div class="form-group"> <label class="col-md-2 control-label">规则名：</label> <div class="col-md-9"> <input class="form-control" name="name" /> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">规则类型：</label> <div class="col-md-9 m-t-xs"> <select name="ruleType" style="width: 100%;"> <option value="">请选择类型</option> ';
if(ruleList!=null && ruleList.length>0){
$out+=' ';
$each(ruleList,function(p,$index){
$out+=' <option value="';
$out+=$escape(p.value);
$out+='" data-id="';
$out+=$escape(p.id);
$out+='" data-code="';
$out+=$escape(p.code);
$out+='">';
$out+=$escape(p.name);
$out+='</option> ';
});
$out+=' ';
}
$out+=' </select> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">候选列：</label> <div class="col-md-9" id="candidatesBox" style="margin-top: 8px;"> </div> </div> <div class="form-group"> <div class="col-md-9 col-md-offset-2"> <button class="btn btn-default" data-action="save">保存</button> <button class="btn btn-default" data-action="back">返回</button> </div> </div> </div> </form>  ';
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_rules_candidates',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,candidatesList=$data.candidatesList,$each=$utils.$each,c=$data.c,$index=$data.$index,$escape=$utils.$escape,v=$data.v,$out='';if(candidatesList!=null && candidatesList.length>0){
$out+=' <table class="table table-bordered table-responsive"> <tbody> ';
$each(candidatesList,function(c,$index){
$out+=' <tr> <td width="20%"> <label class="i-checks fw-normal m-b-none"> <input name="candidates" type="checkbox" data-id="';
$out+=$escape(c.id);
$out+='" data-value="';
$out+=$escape(c.value);
$out+='" data-code="';
$out+=$escape(c.code);
$out+='"`/> <span class="i-checks-span">';
$out+=$escape(c.name);
$out+='</span> </label> </td> <td width="80%"> ';
if(c.valueList!=null && c.valueList.length>0){
$out+=' ';
$each(c.valueList,function(v,$index){
$out+=' <label class="i-checks fw-normal m-b-none"> <input name="candidatesItem" type="checkbox" data-id="';
$out+=$escape(v.id);
$out+='" data-code="';
$out+=$escape(v.code);
$out+='"/> <span class="i-checks-span">';
$out+=$escape(v.valueName);
$out+='</span> </label> ';
});
$out+=' ';
}
$out+=' </td> </tr> ';
});
$out+=' </tbody> </table> ';
}
return new String($out);
});/*v:1*/
template('m_configuration/m_configuration_views',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,viewList=$data.viewList,$each=$utils.$each,v=$data.v,$index=$data.$index,$escape=$utils.$escape,o=$data.o,c=$data.c,ci=$data.ci,$out='';$out+='<table class="table table-striped table-bordered table-hover"> <thead> <tr> <th>父模块</th> <th>子模块</th> <th>操作</th> </tr> </thead> <tbody class="m-page-data"> ';
if(viewList!=null && viewList.length>0){
$out+=' ';
$each(viewList,function(v,$index){
$out+=' <tr> <td rowspan="';
$out+=$escape(v.childList.length>0?v.childList.length:'');
$out+='">';
$out+=$escape(v.viewName);
$out+='</td> <td> ';
if(v.childList.length>0){
$out+=' <label class="i-checks fw-normal m-b-none"> ';
if(v.childList[0].isSelect==1){
$out+=' <input name="moduleCk" type="checkbox" data-id="';
$out+=$escape(v.childList[0].id);
$out+='" checked/> ';
}else{
$out+=' <input name="moduleCk" type="checkbox" data-id="';
$out+=$escape(v.childList[0].id);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(v.childList[0].viewName);
$out+='</span> </label> ';
}
$out+=' </td> <td> ';
if(v.childList.length>0 && v.childList[0].operateList.length>0){
$out+=' ';
$each(v.childList[0].operateList,function(o,$index){
$out+=' ';
if(o.operateName !=null){
$out+=' <label class="i-checks fw-normal m-b-none"> ';
if(o.operateSelect==1){
$out+=' <input name="operateName" type="checkbox" data-id="';
$out+=$escape(o.id);
$out+='" checked/> ';
}else{
$out+=' <input name="operateName" type="checkbox" data-id="';
$out+=$escape(o.id);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(o.operateName);
$out+='</span> </label> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </td> </tr> ';
if(v.childList.length>1 ){
$out+=' ';
$each(v.childList,function(c,ci){
$out+=' ';
if(ci>0){
$out+=' <tr> <td> <label class="i-checks fw-normal m-b-none"> ';
if(c.isSelect==1){
$out+=' <input name="moduleCk" type="checkbox" data-id="';
$out+=$escape(c.id);
$out+='" checked/> ';
}else{
$out+=' <input name="moduleCk" type="checkbox" data-id="';
$out+=$escape(c.id);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(c.viewName);
$out+='</span> </label> </td> <td> ';
if(c.operateList.length>0){
$out+=' ';
$each(c.operateList,function(o,$index){
$out+=' ';
if(o.operateName !=null){
$out+=' <label class="i-checks fw-normal m-b-none"> ';
if(o.operateSelect==1){
$out+=' <input name="operateName" type="checkbox" data-id="';
$out+=$escape(o.id);
$out+='" checked/> ';
}else{
$out+=' <input name="operateName" type="checkbox" data-id="';
$out+=$escape(o.id);
$out+='"/> ';
}
$out+=' <span class="i-checks-span">';
$out+=$escape(o.operateName);
$out+='</span> </label> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </td> </tr> ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' ';
});
$out+=' ';
}
$out+=' </tbody> </table> ';
return new String($out);
});/*v:1*/
template('m_orgAuth/m_orgAuth_list','<div class="page-content" style="padding-top: 0;">  <div class="row"> <div class="col-md-12"> <div class="portlet light" style="overflow: hidden;"> <div class="portlet-title"> <div class="caption">  <span class="caption-subject bold font-yellow-casablanca uppercase">企业认证审核</span> </div> <div class="actions"> <a class="btn btn-circle btn-icon-only btn-default fullscreen" href="javascript:void(0);"> </a> </div> <div class="inputs" style="margin-right: 10px;"> <div class="portlet-input input-inline input-medium"> <div class="input-group"> <input type="text" class="form-control input-circle-left search-input" placeholder="请输入关键字"> <span class="input-group-btn"> <button class="btn btn-circle-right btn-default" data-action="search">搜索</button> </span> </div> </div> </div> </div> <div class="portlet-body"> <table class="table table-striped table-bordered table-hover" id="list"> <thead> <tr> <th>序号</th> <th>组织名称</th> <th>企业名称</th> <th>证件类型</th> <th>注册号/统一社会代码</th> <th>法人</th> <th>经办人</th> <th name="field_applyDate">认证时间</th> <th class="text-center">审核状态</th> <th>审核人</th> <th name="field_auditDate">审核时间</th> </tr> </thead> <tbody class="m-page-data"> </tbody> </table> </div> <div class="clearfix"></div> <div id="orgAuthList_sortSwap"></div> <div class="m-page pull-right" style="margin-bottom: 20px;"></div> </div> </div> </div> </div>');/*v:1*/
template('m_orgAuth/m_orgAuth_list_row',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,list=$data.list,o=$data.o,i=$data.i,$escape=$utils.$escape,pageIndex=$data.pageIndex,pageSize=$data.pageSize,_isNullOrBlank=$helpers._isNullOrBlank,_isNowDiffTargetDateNDaysMore=$helpers._isNowDiffTargetDateNDaysMore,$out='';$each(list,function(o,i){
$out+=' <tr class="odd gradeX"> <td>';
$out+=$escape(pageIndex*pageSize+i+1);
$out+='</td> <td>';
$out+=$escape(o.orgAlias);
$out+='</td> <td>';
$out+=$escape(o.orgName);
$out+='</td> <td style="width: 150px;max-width: 150px;"> ';
if(o.businessLicenseType===0){
$out+=' 普通营业执照 ';
}else if(o.businessLicenseType===1){
$out+=' 多证合一营业执照 ';
}
$out+=' ';
if(!_isNullOrBlank(o.legalRepresentativePhoto)){
$out+=' <a href="javascript:void(0);" data-action="preview" data-url="';
$out+=$escape(o.legalRepresentativePhoto);
$out+='"><i class="fa fa-file-image-o"></i></a> ';
}
$out+=' </td> <td>';
$out+=$escape(o.businessLicenseNumber);
$out+='</td> <td style="width: 80px;max-width: 80px;"> ';
$out+=$escape(o.legalRepresentative);
$out+=' ';
if(!_isNullOrBlank(o.businessLicensePhoto)){
$out+=' <a href="javascript:void(0);" data-action="preview" data-url="';
$out+=$escape(o.businessLicensePhoto);
$out+='"><i class="fa fa-file-image-o"></i></a> ';
}
$out+=' </td> <td style="width: 80px;max-width: 80px;"> ';
$out+=$escape(o.operatorName);
$out+=' ';
if(!_isNullOrBlank(o.operatorPhoto)){
$out+=' <a href="javascript:void(0);" data-action="preview" data-url="';
$out+=$escape(o.operatorPhoto);
$out+='"><i class="fa fa-file-image-o"></i></a> ';
}
$out+=' </td> <td style="width: 120px;max-width: 120px;"> ';
if(o.authenticationStatus === 1 && _isNowDiffTargetDateNDaysMore(o.applyDate,3)){
$out+=' <span style="color: red;">';
$out+=$escape(o.applyDate);
$out+='</span> ';
}else{
$out+=' ';
$out+=$escape(o.applyDate);
$out+=' ';
}
$out+=' ';
if(!_isNullOrBlank(o.sealPhoto)){
$out+=' <a href="javascript:void(0);" data-action="preview" data-url="';
$out+=$escape(o.sealPhoto);
$out+='"><i class="fa fa-file-image-o"></i></a> ';
}
$out+=' </td> <td style="width: 90px;max-width: 90px;padding-left:15px;padding-right: 15px;"> ';
if(o.authenticationStatus === 0){
$out+=' <span class="label label-sm label-warning span-tag-60-25" style="width: 60px;">未提交</span> ';
}else if(o.authenticationStatus === 1){
$out+=' <div class="btn-toolbar"> <div class="btn-group"> <button class="btn btn-xs btn-info dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false" style="width: 60px;"> 审核 </button> <ul class="dropdown-menu dropdown-menu-left dropdown-menu-v1" role="menu"> <li> <a href="javascript:void(0);" data-action="audit_pass" data-id="';
$out+=$escape(o.id);
$out+='">通过</a> </li> <li> <a href="javascript:void(0);" data-action="audit_reject" data-id="';
$out+=$escape(o.id);
$out+='">不通过</a> </li> </ul> </div> </div> ';
}else if(o.authenticationStatus === 2){
$out+='  <div class="btn-toolbar"> <div class="btn-group"> <button class="btn btn-xs btn-success dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false" style="width: 60px;"> 已通过 </button> <ul class="dropdown-menu dropdown-menu-left dropdown-menu-v1" role="menu"> <li> <a href="javascript:void(0);" data-action="audit_reject" data-id="';
$out+=$escape(o.id);
$out+='">重置为不通过</a> </li> </ul> </div> </div> ';
}else if(o.authenticationStatus === 3){
$out+='  <div class="btn-toolbar"> <div class="btn-group"> <button class="btn btn-xs btn-danger dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false" style="width: 60px;"> 不通过 </button> <ul class="dropdown-menu dropdown-menu-left dropdown-menu-v1" role="menu"> <li> <a href="javascript:void(0);" data-action="audit_view" data-id="';
$out+=$escape(o.id);
$out+='" data-reject-type="';
$out+=$escape(o.rejectType);
$out+='" data-reject-reason="';
$out+=$escape(o.rejectReason);
$out+='" data-auditor-name="';
$out+=$escape(o.auditorName);
$out+='">查看原因</a> </li> <li> <a href="javascript:void(0);" data-action="audit_pass" data-id="';
$out+=$escape(o.id);
$out+='">重置为通过</a> </li> </ul> </div> </div> ';
}
$out+=' </td> <td>';
$out+=$escape(o.auditorName);
$out+='</td> <td>';
$out+=$escape(o.auditDate);
$out+='</td> </tr> ';
});
return new String($out);
});/*v:1*/
template('m_orgAuth/m_orgAuth_reject','<div class="portlet light" style="overflow: hidden;"> <div class="portlet-title"> <div class="caption">  <span class="caption-subject bold font-yellow-casablanca uppercase">不通过原因</span> </div> <div class="tools"> <a href="" class="remove" data-original-title="" title=""> </a> </div> </div> <div class="portlet-body"> <form class="form-horizontal" role="form"> <div class="form-body"> <div class="form-group"> <label class="col-md-2 control-label">原因：</label> <div class="col-md-9"> <select id="rejectType"></select> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">说明：</label> <div class="col-md-9"> <textarea id="rejectReason" class="form-control" rows="5"></textarea> </div> </div> </div> </form> </div> <div class="modal-footer"> <button type="button" class="btn btn-success" data-action="layer-custom-btn-yes">确&nbsp;定</button> <button type="button" class="btn btn-default" data-action="layer-custom-btn-close">取&nbsp;消</button> </div> </div>');/*v:1*/
template('m_orgAuth/m_orgAuth_view','<div class="portlet light" style="overflow: hidden;"> <div class="portlet-title"> <div class="caption">  <span class="caption-subject bold font-yellow-casablanca uppercase">不通过原因</span> </div> <div class="tools"> <a href="" class="remove" data-original-title="" title=""> </a> </div> </div> <div class="portlet-body"> <form class="form-horizontal" role="form"> <div class="form-body"> <div class="form-group"> <label class="col-md-2 control-label">原因：</label> <div class="col-md-9"> <select id="rejectType"></select> </div> </div> <div class="form-group"> <label class="col-md-2 control-label">说明：</label> <div class="col-md-9"> <textarea id="rejectReason" class="form-control" rows="5"></textarea> </div> </div> </div> </form> </div> <div class="modal-footer"> <button type="button" class="btn btn-default" data-action="layer-custom-btn-close">关&nbsp;闭</button> </div> </div>');/*v:1*/
template('m_upload/m_uploadmgr','<div class="uploadmgr tag-box tag-box-v1 box-shadow shadow-effect-1"> <div class="alertmgr"></div> <a href="javascript:void(0)" class="btn-select btn-u btn-u-sm btn-u-orange rounded" type="button"><i class="fa fa-plus"></i>&nbsp;选择文件</a> <a href="javascript:void(0)" class="btn-start btn-u btn-u-sm btn-u-green2 rounded dp-none" type="button"><i class="fa fa-caret-right"></i>&nbsp;开始</a> <a href="javascript:void(0)" class="btn-stop btn-u btn-u-sm btn-u-red rounded dp-none" type="button"><i class="fa fa-times"></i>&nbsp;停止</a> <a href="javascript:void(0)" type="button" class="btn-close close">×</a> <div class="upload-item-list"> </div> <p class="pull-right"></p> </div>');/*v:1*/
template('m_upload/m_uploadmgr_uploadItem',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$escape=$utils.$escape,file=$data.file,pid=$data.pid,$out='';$out+='<div class="uploadItem uploadItem_';
$out+=$escape(file.id);
$out+='" data-fileId="';
$out+=$escape(file.id);
$out+='" data-pid="';
$out+=$escape(pid);
$out+='"> <button type="button" class="close removefile">×</button> <h3 class="heading-xs">';
$out+=$escape(file.name);
$out+='<span style="padding-left:5px;" class="span_progress"></span> <span class="span_status pull-right m-r-sm"></span></h3> <div class="progress progress-u progress-xs"> <div class="progress-bar progress-bar-u" role="progressbar" name="div_progress" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%"> </div> </div> </div>';
return new String($out);
});

}()