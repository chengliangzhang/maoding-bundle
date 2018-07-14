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
    /*v:1*/
template('m_setting/m_setting_list',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,list=$data.list,o=$data.o,i=$data.i,$escape=$utils.$escape,_isNullOrBlank=$helpers._isNullOrBlank,_formatFileSize=$helpers._formatFileSize,$out='';$each(list,function(o,i){
$out+=' <tr> <td>';
$out+=$escape(o.corpEndpoint);
$out+='</td> <td>';
$out+=$escape(o.companyId);
$out+='</td> <td>';
$out+=$escape((_isNullOrBlank(o.companyName)?'<span style="color:red;">无法匹配</span>':o.companyName));
$out+='</td> <td>总空间：';
$out+=$escape(_formatFileSize(o.totalSize));
$out+=' <br/> 协同：';
$out+=$escape(_formatFileSize(o.corpSize));
$out+=' ';
$out+=$escape(o.corpOnCloud===true?'（云端部署）':'（本地部署）');
$out+=' <br/> 文档库：';
$out+=$escape(_formatFileSize(o.docmgrSize));
$out+=' <br/> 其他：';
$out+=$escape(_formatFileSize(o.otherSize));
$out+=' <br/> 可用空间：';
$out+=$escape(_formatFileSize(o.freeSize));
$out+='<br/> <a name="btnSwitchDeployCorp" href="javacript:void(0)" data-company-id="';
$out+=$escape(o.companyId);
$out+='" class="btn btn-warning btn-xs">切换协同部署方式</a> <a name="btnRecalcSize" href="javacript:void(0)" data-company-id="';
$out+=$escape(o.companyId);
$out+='" class="btn btn-success btn-xs">重新统计文档库和其他</a><br/> </td> <td>';
$out+=$escape(o.remarks);
$out+='</td> <td> <a name="btnRemove" href="javacript:void(0)" data-id="';
$out+=$escape(o.id);
$out+='" class="btn btn-danger btn-sm ">删除</a>&nbsp;<a name="btnSync" href="javacript:void(0)" data-id="';
$out+=$escape(o.id);
$out+='" class="btn btn-success btn-sm ">立即同步</a> </td> </tr> ';
});
return new String($out);
});

}()