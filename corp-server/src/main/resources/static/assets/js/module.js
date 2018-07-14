/**
 * Created by Wuwq on 2017/2/27.
 */
var restApi = {
    /** syncCompany **/
    url_syncCompany_selectAll: '/corpserver/syncCompany/selectAll'
    , url_syncCompany_create: '/corpserver/syncCompany/create'
    , url_syncCompany_delete: '/corpserver/syncCompany/delete'
    , url_syncCompany_pushSyncAllCmd: '/corpserver/syncCompany/pushSyncAllCmd'

    /** companyDisk **/
    , url_companyDisk_recalcSizeByCompanyId: '/corpserver/companyDisk/recalcSizeByCompanyId'
    , url_companyDisk_switchCorpDeployType: '/corpserver/companyDisk/switchCorpDeployType'
};
/* Write here your custom javascript codes */

/*阻止默认事件*/
var preventDefault = function (event) {
    if (event.preventDefault) {
        event.preventDefault();
    } else {
        event.returnValue = false;
    }
};

/*阻止冒泡*/
var stopPropagation = function (event) {
    if (event.stopPropagation) {
        event.stopPropagation();
    } else {
        event.cancelBubble = true;
    }
};

/*短格式时间
 * 1：00
 * 2：00
 * */
var shortTime = function (datetime) {
    return moment(datetime).format("HH:mm");
};


/*格式化日期
 * 今天 1：00
 * 昨天 2：00
 * 2017-01-01 2：00
 * */
var dateSpecFormat = function (datetime, pattern) {
    var now = moment(new Date(), 'YYYY-MM-DD HH:mm:ss');
    var yesterday = moment(new Date(), 'YYYY-MM-DD HH:mm:ss').subtract(1, 'days');
    var d = moment(moment(datetime).toDate(), 'YYYY-MM-DD HH:mm:ss');

    var nowFormat = now.format('YYYY-MM-DD');
    var yesterdayFormat = yesterday.format('YYYY-MM-DD');
    var dFormat = d.format('YYYY-MM-DD');

    var t1 = '';
    if (nowFormat == dFormat)
        t1 = '今天';
    else if (yesterdayFormat == dFormat)
        t1 = '昨天';
    else
        t1 = dFormat;

    if (pattern && !_.isBlank(pattern))
        return _.sprintf(pattern, t1, d.format('HH:mm'));

    return _.sprintf('%s %s', t1, d.format('HH:mm'));
};

/*格式化日期
 * 今天
 * 昨天
 * 2017-01-01
 * */
var dateSpecShortFormat = function (datetime) {
    var now = moment(new Date(), 'YYYY-MM-DD HH:mm:ss');
    var yesterday = moment(new Date(), 'YYYY-MM-DD HH:mm:ss').subtract(1, 'days');
    var d = moment(moment(datetime).toDate(), 'YYYY-MM-DD HH:mm:ss');

    var nowFormat = now.format('YYYY-MM-DD');
    var yesterdayFormat = yesterday.format('YYYY-MM-DD');
    var dFormat = d.format('YYYY-MM-DD');

    var t1 = '';
    if (nowFormat == dFormat)
        t1 = '今天';
    else if (yesterdayFormat == dFormat)
        t1 = '昨天';
    else
        t1 = dFormat;

    return t1;
};

/*判断字符串是否为undefined、Null或空*/
var isNullOrBlank = function (str) {
    return str === void 0 || str === null || _.isBlank(str);
};

//处理IE的Console.log兼容问题
(function () {
    var method;
    var noop = function () {
    };
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error', 'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log', 'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd', 'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});
    while (length--) {
        method = methods[length];
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());
//form数据提交
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
/**
 * 替换一段话中相匹配的字符串
 * g是全局m是多行
 * @param s1 处理的字符串
 * @param s2 替换的字符串
 * @returns
 */
String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
};

/**
 * 两日期对比
 * @param d1 时间一
 * @param d2 时间二
 * @returns {Number}
 */
function dateDiff(d1, d2) {
    if (!d1 || d1 == null || d1 == '' || !d2 || d2 == null || d2 == '') {
        return 0;
    }
    var result = Date.parse(d1.toString().replace(/-/g, "/")) - Date.parse(d2.toString().replace(/-/g, "/"));
    return result;
}
//获取当前日期
function getNowDate() {
    var date = new Date();
    var year = date.getFullYear(),
        mon = date.getMonth() < 9 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1,
        day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate(),
        nowDate = year + "-" + mon + "-" + day;
    return nowDate;
}
/**
 * 时间差
 * @param stime
 * @param etime
 */
function diffDays(stime, etime) {
    var d1 = this.dateDiff(etime, stime);
    var day1 = Math.floor(d1 / (24 * 3600 * 1000));
    return day1;
}

/**
 * 获取字符串长度，中文按2个字节，英文按1个字节
 */
function getStringLength(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        //单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            len++;
        }
        else {
            len += 2;
        }
    }
    return len;
}

/**
 * 获取字符串长度，中文按2个字节，英文按1个字节
 */
var cutString = function (str, length, suffix) {
    if (isNullOrBlank(str))
        return '';

    var len = 0;
    var temp = '';
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        //单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
            len++;
        }
        else {
            len += 2;
        }
        temp += str.charAt(i);
        if (len >= length)
            return temp + suffix;
    }
    return str;
};

/*****************************验证公共方法--结束**********************************/

//数据提交访问错误
function handlePostJsonError(response) {
    if (response.status == 404) {
        //当前请求地址未找到
        S_toastr.error('当前请求地址未找到！');

    } else if (response.status == 0) {
        //网络请求超时
        S_toastr.error('网络请求超时！');
    } else {
        S_toastr.error('网络请求出现错误！status：' + response.status + "，statusText：" + response.statusText);
    }
    //var text = '访问出现异常！<br/>status: ' + response.status + '<br/>statusText: ' + response.statusText;
    //S_dialog.alert(text);
}
//数据提交访问错误
function handleResponse(response) {
    var result = false;
    if (response.code == "401") {
        //session超时 !
        S_dialog.error('当前用户状态信息已超时!点击“确定”后返回登录界面。', '提示', function () {
            window.location.href = rootPath + '/iWork/sys/login';
        });
        result = true;
    } else if (response.code == "500") {
        //未捕获异常 X
        S_dialog.error('出现异常错误 !详细信息：' + response.info);
        result = true;
    }
    return result;
}


var S_toastr = {
    success:function (text) {
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "progressBar": false,
            "preventDuplicates": false,
            "positionClass": "toast-top-center",
            "onclick": null,
            "showDuration": "400",
            "hideDuration": "1000",
            "timeOut": "3000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.success(text);
    },
    warning:function (text) {
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "progressBar": false,
            "preventDuplicates": false,
            "positionClass": "toast-top-center",
            "onclick": null,
            "showDuration": "400",
            "hideDuration": "1000",
            "timeOut": "3000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.warning(text);
    },
    info:function (text) {
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "progressBar": false,
            "preventDuplicates": false,
            "positionClass": "toast-top-center",
            "onclick": null,
            "showDuration": "400",
            "hideDuration": "1000",
            "timeOut": "3000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.info(text);
    },
    error:function (text) {
        toastr.options = {
            "closeButton": false,
            "debug": false,
            "progressBar": false,
            "preventDuplicates": false,
            "positionClass": "toast-top-center",
            "onclick": null,
            "showDuration": "400",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.error(text);
    }
};

var S_swal = {
    confirm:function (option) {
        swal({
            title: option.title||"确定此操作吗?",
            text: option.text,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: option.confirmButtonText||"确定",
            cancelButtonText: option.cancelButtonText||"取消",
            closeOnConfirm:  option.closeOnConfirm||false
        }, function () {
            if(option.callBack!=null){
                option.callBack();
            }
        });
    },
    sure:function (option) {
        swal({
            title: option.title||"确定此操作吗?",
            text: option.text,
            type: "success",
            confirmButtonText: option.confirmButtonText||"确定",
            closeOnConfirm: true
        }, function () {
            if(option.callBack!=null){
                option.callBack();
            }
        });
    }
};

/******************************************** 弹窗方法 结束 *****************************************************/


var m_ajax = {
    get: function (option, onHttpSuccess, onHttpError) {
        $.ajax({
            type: 'GET',
            url: option.url,
            cache: false,
            beforeSend: function () {
                if (option.classId)
                    $_loading.show(option.classId, '正在加载中...');

                if (option.bindDisabled) {
                    var $el = $(option.bindDisabled);
                    if ($el.length > 0) {
                        try {
                            $el.attr('disabled', true);
                        } catch (e) {
                        }
                        try {
                            $el.prop('disabled', true);
                        } catch (e) {
                        }
                    }
                }
            },
            success: function (response) {

                if (!handleResponse(response)) {
                    if (onHttpSuccess)
                        onHttpSuccess(response);
                }

            },
            error: function (response) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(response);
                //else
                //tzTips.showOnTopRight("Ajax请求发生错误", "error");
            },
            complete: function () {
                if (option.classId)
                    $_loading.close(option.classId);

                if (option.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(option.bindDisabled);
                        if ($el.length > 0) {
                            try {
                                $el.attr('disabled', false);
                            } catch (e) {
                            }
                            try {
                                $el.prop('disabled', false);
                            } catch (e) {
                            }
                        }
                    }, 1000);

                }
            }
        });
    },
    getJson: function (option, onHttpSuccess, onHttpError) {
        $.ajax({
            type: 'GET',
            url: option.url,
            cache: false,
            contentType: "application/json",
            beforeSend: function () {
                if (option.classId)
                    $_loading.show(option.classId, '正在加载中...');

                if (option.bindDisabled) {
                    var $el = $(option.bindDisabled);
                    if ($el.length > 0) {
                        try {
                            $el.attr('disabled', true);
                        } catch (e) {
                        }
                        try {
                            $el.prop('disabled', true);
                        } catch (e) {
                        }
                    }
                }
            },
            success: function (response) {

                if (!handleResponse(response)) {
                    if (onHttpSuccess)
                        onHttpSuccess(response);
                }

            },
            error: function (response) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(response);
                //else
                //tzTips.showOnTopRight("Ajax请求发生错误", "error");
            },
            complete: function () {
                if (option.classId)
                    $_loading.close(option.classId);

                if (option.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(option.bindDisabled);
                        if ($el.length > 0) {
                            try {
                                $el.attr('disabled', false);
                            } catch (e) {
                            }
                            try {
                                $el.prop('disabled', false);
                            } catch (e) {
                            }
                        }
                    }, 1000);

                }
            }
        });
    },
    post: function (option, onHttpSuccess, onHttpError) {
        //var pNotify;
        $.ajax({
            type: 'POST',
            url: option.url,
            data: option.postData,
            cache: false,
            beforeSend: function () {
                if (option.classId)
                    $_loading.show(option.classId, '正在加载中...');

                if (option.bindDisabled) {
                    var $el = $(option.bindDisabled);
                    if ($el.length > 0) {
                        try {
                            $el.attr('disabled', true);
                        } catch (e) {
                        }
                        try {
                            $el.prop('disabled', true);
                        } catch (e) {
                        }
                    }
                }
            },
            success: function (response) {

                if (!handleResponse(response)) {
                    if (onHttpSuccess)
                        onHttpSuccess(response);
                }

            },
            error: function (response) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(response);
                //else
                //tzTips.showOnTopRight("Ajax请求发生错误", "error");
            },
            complete: function () {
                if (option.classId)
                    $_loading.close(option.classId);

                if (option.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(option.bindDisabled);
                        if ($el.length > 0) {
                            try {
                                $el.attr('disabled', false);
                            } catch (e) {
                            }
                            try {
                                $el.prop('disabled', false);
                            } catch (e) {
                            }
                        }
                    }, 1000);

                }
            }
        });
    },
    postJson: function (option, onHttpSuccess, onHttpError) {
        $.ajax({
            type: 'POST',
            url: option.url,
            cache: false,
            async: option.async == null ? true : option.async,
            data: JSON.stringify(option.postData),
            contentType: "application/json",
            beforeSend: function () {
                if (option.classId)
                    $_loading.show(option.classId, '正在加载中...');

                if (option.bindDisabled) {
                    var $el = $(option.bindDisabled);
                    if ($el.length > 0) {
                        try {
                            $el.attr('disabled', true);
                        } catch (e) {
                        }
                        try {
                            $el.prop('disabled', true);
                        } catch (e) {
                        }
                    }
                }
            },
            success: function (response) {
                if (!handleResponse(response)) {
                    if (onHttpSuccess)
                        onHttpSuccess(response);
                }
            },
            error: function (response) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(response);
                //else
                //tzTips.showOnTopRight("Ajax请求发生错误", "error");
            },
            complete: function () {
                if (option.classId)
                    $_loading.close(option.classId);

                if (option.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(option.bindDisabled);
                        if ($el.length > 0) {
                            try {
                                $el.attr('disabled', false);
                            } catch (e) {
                            }
                            try {
                                $el.prop('disabled', false);
                            } catch (e) {
                            }
                        }
                    }, 1000);
                }
            }
        });
    },
    delete: function (option, onHttpSuccess, onHttpError) {
        //var pNotify;
        $.ajax({
            type: 'DELETE',
            url: option.url,
            cache: false,
            data: JSON.stringify(option.postData),
            contentType: "application/json",
            beforeSend: function () {
                if (option.classId)
                    $_loading.show(option.classId, '正在加载中...');

                if (option.bindDisabled) {
                    var $el = $(option.bindDisabled);
                    if ($el.length > 0) {
                        try {
                            $el.attr('disabled', true);
                        } catch (e) {
                        }
                        try {
                            $el.prop('disabled', true);
                        } catch (e) {
                        }
                    }
                }
            },
            success: function (response) {

                if (!handleResponse(response)) {
                    if (onHttpSuccess)
                        onHttpSuccess(response);
                }

            },
            error: function (response) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(response);
                //else
                //tzTips.showOnTopRight("Ajax请求发生错误", "error");
            },
            complete: function () {
                if (option.classId)
                    $_loading.close(option.classId);

                if (option.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(option.bindDisabled);
                        if ($el.length > 0) {
                            try {
                                $el.attr('disabled', false);
                            } catch (e) {
                            }
                            try {
                                $el.prop('disabled', false);
                            } catch (e) {
                            }
                        }
                    }, 1000);
                }
            }
        });
    }
};

//num表示要四舍五入的数,v表示要保留的小数位数。
function decimal(num, v) {
    var vv = Math.pow(10, v);
    return Math.round(num * vv) / vv;
}

//计算应显示的小数位
var countDigits = function (val, maxDigits) {
    var splits = (val - 0).toString().split(".");
    var digits = 0;
    if (splits.length > 1) {
        if (splits[1].length < maxDigits)
            digits = splits[1].length;
        else
            digits = maxDigits;
    }
    return digits;
};

//精准计算
var doMath = {
    //精确减法
    accSub: function (a1, a2) {
        var r1, r2, m, n;
        try {
            r1 = a1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = a2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        //动态控制精度长度
        n = (r1 >= r2) ? r1 : r2;
        return ((a1 * m - a2 * m) / m).toFixed(n);
    },
    //精确加法
    accAdd: function (a1, a2) {
        var r1, r2, m;
        try {
            r1 = a1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = a2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        return (a1 * m + a2 * m) / m;
    }
};

//封装成Number类型的子方法，调用方法如: 8-4 写成 8.sub(4)
Number.prototype.sub = function (arg) {//减法
    return parseFloat(doMath.accSub(this, arg));
};
Number.prototype.add = function (arg) {//加法
    return parseFloat(doMath.accAdd(this, arg));
};


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
/**
 * Created by Wuwq on 2017/1/5.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_setting",
        defaults = {};

    function Plugin(element, options) {
        this.element = element;

        this.settings = options;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._render();
        }
        , _render: function () {
            var that = this;
            var html = template('xxx', {});
            $(that.element).html(html);
        }
    });

    /*
     1.一般初始化（缓存单例）： $('#id').pluginName(initOptions);
     2.强制初始化（无视缓存）： $('#id').pluginName(initOptions,true);
     3.调用方法： $('#id').pluginName('methodName',args);
     */
    $.fn[pluginName] = function (options, args) {
        var instance;
        var funcResult;
        var jqObj = this.each(function () {

            //从缓存获取实例
            instance = $.data(this, "plugin_" + pluginName);

            if (options === undefined || options === null || typeof options === "object") {

                var opts = $.extend(true, {}, defaults, options);

                //options作为初始化参数，若args===true则强制重新初始化，否则根据缓存判断是否需要初始化
                if (args === true) {
                    instance = new Plugin(this, opts);
                } else {
                    if (instance === undefined || instance === null)
                        instance = new Plugin(this, opts);
                }

                //写入缓存
                $.data(this, "plugin_" + pluginName, instance);
            }
            else if (typeof options === "string" && typeof instance[options] === "function") {

                //options作为方法名，args则作为方法要调用的参数
                //如果方法没有返回值，funcReuslt为undefined
                funcResult = instance[options].call(instance, args);
            }
        });

        return funcResult === undefined ? jqObj : funcResult;
    };

})(jQuery, window, document);
/**
 * Created by Wuwq on 2017/05/26.
 */
var corpserver_setting = {
    init: function () {
        var that = this;
        that.bindAdd();
        that.refreshData();
    },
    refreshData: function () {
        var that = this;
        var ajaxOpts = {
            url: restApi.url_syncCompany_selectAll
        };
        m_ajax.getJson(ajaxOpts, function (res) {
            if (res.code === '0') {
                $('#tbody').html('');
                var html = template('m_setting/m_setting_list', {list: res.data});
                $('#tbody').append(html);
                that.bindAction();
            }
            else {
                if (!isNullOrBlank(res.msg))
                    S_toastr.error(res.msg);
                else
                    S_toastr.error("数据请求失败");
            }
        });
    },
    bindAdd: function () {
        var that = this;
        $('#btnAdd').click(function () {
            var corpEndpoint = $.trim($('#corpEndpoint').val());
            var companyId = $.trim($('#companyId').val());
            var remarks = $.trim($('#remarks').val());

            var ajaxOpts = {
                url: restApi.url_syncCompany_create,
                postData: {
                    corpEndpoint: corpEndpoint,
                    companyId: companyId,
                    remarks: remarks
                }
            };
            m_ajax.postJson(ajaxOpts, function (res) {
                if (res.code === '0') {
                    that.refreshData();
                }
                else {
                    if (!isNullOrBlank(res.msg))
                        S_toastr.error(res.msg);
                    else
                        S_toastr.error("数据请求失败");
                }
            });
        });
    }
    , bindAction: function () {
        var that = this;
        $('a[name="btnSwitchDeployCorp"]').click(function () {
            var companyId = $(this).attr('data-company-id');
            var ajaxOpts = {
                url: restApi.url_companyDisk_switchCorpDeployType,
                postData: {
                    companyId: companyId
                }
            };
            m_ajax.postJson(ajaxOpts, function (res) {
                if (res.code === '0') {
                    that.refreshData();
                }
                else {
                    if (!isNullOrBlank(res.msg))
                        S_toastr.error(res.msg);
                    else
                        S_toastr.error("数据请求失败");
                }
            });
        });

        $('a[name="btnRecalcSize"]').click(function () {
            var companyId = $(this).attr('data-company-id');
            var ajaxOpts = {
                url: restApi.url_companyDisk_recalcSizeByCompanyId,
                postData: {
                    companyId: companyId
                }
            };
            m_ajax.postJson(ajaxOpts, function (res) {
                if (res.code === '0') {
                    that.refreshData();
                }
                else {
                    if (!isNullOrBlank(res.msg))
                        S_toastr.error(res.msg);
                    else
                        S_toastr.error("数据请求失败");
                }
            });
        });

        $('a[name="btnRemove"]').click(function () {
            var id = $(this).attr('data-id');
            var ajaxOpts = {
                url: restApi.url_syncCompany_delete + '/' + id
            };
            m_ajax.postJson(ajaxOpts, function (res) {
                if (res.code === '0') {
                    that.refreshData();
                }
                else {
                    if (!isNullOrBlank(res.msg))
                        S_toastr.error(res.msg);
                    else
                        S_toastr.error("数据请求失败");
                }
            });
        });

        $('a[name="btnSync"]').click(function () {
            var id = $(this).attr('data-id');
            var ajaxOpts = {
                url: restApi.url_syncCompany_pushSyncAllCmd + '/' + id
            };
            m_ajax.postJson(ajaxOpts, function (res) {
                if (res.code === '0') {
                    S_toastr.success("已经发送同步指令，同步可能需要1-2分钟")
                }
                else {
                    if (!isNullOrBlank(res.msg))
                        S_toastr.error(res.msg);
                    else
                        S_toastr.error("数据请求失败");
                }
            });
        });
    }
};