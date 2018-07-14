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

