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


var S_layer = {
    dialog: function (options) {
        var defaults = {
            id: '',
            type: 1, /*0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）*/
            title: ' ', /*['文本', 'font-size:18px;'],false*/
            anim: -1, /*弹出动画，0-6*/
            isOutAnim: false, /*关闭动画*/
            time: 0, /*自动关闭所需毫秒*/
            area: 'auto', /*['390px', '330px'],auto*/
            /*maxWidth: '360px', *//*只有当area: 'auto'时，maxWidth的设定才有效。*/
            shade: 0.3,
            shadeClose: false, /*是否点击遮罩关闭*/
            closeBtn: 1, /*关闭按钮 0-2*/
            move: '.layui-layer-title', /*触发拖动的元素,false*/
            moveOut: true, /*是否允许拖拽到窗口外*/
            maxmin: true,
            moveEnd: null, /*function(layero){}*/
            fixed: true, /*鼠标滚动时，层是否固定在可视区域*/
            /*offset: [($(window).height() - 300)/2, ($(window).width() - 390)/2],*/
            content: null,
            btn: ['确定', '取消'],
            btnAlign: 'r', /*按钮位置l-c-r*/
            yes: function (index, layero) {
                layer.close(index);
            },
            cancel: function (index, layero) {
            },
            btn2: function (index, layero) {
                layer.close(index);
                //layer.closeAll();
                //return false 开启该代码可禁止点击该按钮关闭
            },
            resize: false,
            resizing: null, /*function(layero){}*/
            scrollbar: false,
            zIndex: layer.zIndex,
            success: function (layero) {
                layer.setTop(layero);
            },
            full: null, /*function(layero){}*/
            min: null, /*function(layero){}*/
            restore: null, /*function(layero){}*/
            end: null /*function(){}*/
        };
        var opts = $.extend(true, {}, defaults, options);
        layer.open(opts);
    },
    panel: function (options) {
        var defaults = {
            id: '',
            type: 1, /*0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）*/
            title: false, /*['文本', 'font-size:18px;'],false*/
            anim: -1, /*弹出动画，0-6*/
            isOutAnim: false, /*关闭动画*/
            time: 0, /*自动关闭所需毫秒*/
            area: 'auto', /*['390px', '330px'],auto*/
            /*maxWidth: '360px', *//*只有当area: 'auto'时，maxWidth的设定才有效。*/
            shade: 0.3,
            shadeClose: false, /*是否点击遮罩关闭*/
            closeBtn: 0, /*关闭按钮 0-2*/
            move: '.layui-layer-title', /*触发拖动的元素,false*/
            moveOut: true, /*是否允许拖拽到窗口外*/
            maxmin: false,
            moveEnd: null, /*function(layero){}*/
            fixed: true, /*鼠标滚动时，层是否固定在可视区域*/
            /*offset: [($(window).height() - 300)/2, ($(window).width() - 390)/2],*/
            content: null,
            btn: null,
            btnAlign: 'r', /*按钮位置l-c-r*/
            cancel: function (index, layero) {
            },
            shown: null, /*function(layero){}*/
            resize: false,
            resizing: null, /*function(layero){}*/
            scrollbar: false,
            zIndex: layer.zIndex,
            full: null, /*function(layero){}*/
            min: null, /*function(layero){}*/
            restore: null, /*function(layero){}*/
            end: null /*function(){}*/
        };
        var opts = $.extend(true, {}, defaults, options);
        opts.type = 1;
        opts.title = false;
        opts.move = '.portlet-title';
        opts.btn = null;
        opts.maxmin = null;
        opts.closeBtn = 0;
        opts.success = function (layero) {
            layer.setTop(layero);

            var $btnYes = layero.find('button[data-action="layer-custom-btn-yes"]');
            if ($btnYes && $btnYes.length > 0) {
                $btnYes.off('click.yes').on('click.yes', function () {
                    if (opts.yes !== void 0 && opts.yes !== null)
                        opts.yes(layer.index, layero);
                });
            }

            var $btnClose = layero.find('button[data-action="layer-custom-btn-close"]');
            if ($btnClose && $btnClose.length > 0) {
                $btnClose.off('click.close').on('click.close', function () {
                    layer.close(layer.index);
                });
            }

            if (opts.shown !== void 0 && opts.shown !== null)
                opts.shown(layer.index, layero);
        };
        layer.open(opts);
    }
};

var S_swal = {
    confirm: function (options, callback) {
        var defaults = {
            title: '您确定要进行该操作吗？',
            text: '',
            type: 'info',
            allowOutsideClick: true,
            showConfirmButton: true,
            showCancelButton: true,
            confirmButtonClass: 'btn-success',
            cancelButtonClass: 'btn-danger',
            closeOnConfirm: true,
            closeOnCancel: true,
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        };
        var opts = $.extend(true, {}, defaults, options);
        swal(opts, callback);
    }
};

var S_toastr = {
    success: function (text) {
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
    warning: function (text) {
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
    info: function (text) {
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
    error: function (text) {
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
    },
    clear: function () {
        toastr.clear();
    }
};

var S_loading = {
    _blockUI: function (options) {
        options = $.extend(true, {}, options);
        var html = '';
        if (options.animate) {
            html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '">' + '<div class="block-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>' + '</div>';
        } else if (options.iconOnly) {
            html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><img src="' + this.getGlobalImgPath() + 'loading-spinner-grey.gif" align=""></div>';
        } else if (options.textOnly) {
            html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><span>&nbsp;&nbsp;' + (options.message ? options.message : 'LOADING...') + '</span></div>';
        } else {
            html = '<div class="loading-message ' + (options.boxed ? 'loading-message-boxed' : '') + '"><img src="' + this.getGlobalImgPath() + 'loading-spinner-grey.gif" align=""><span>&nbsp;&nbsp;' + (options.message ? options.message : 'LOADING...') + '</span></div>';
        }

        if (options.target) { // element blocking
            var el = $(options.target);
            if (el.height() <= ($(window).height())) {
                options.cenrerY = true;
            }
            el.block({
                message: html,
                baseZ: options.zIndex ? options.zIndex : 1000,
                centerY: options.cenrerY !== undefined ? options.cenrerY : false,
                css: {
                    top: '10%',
                    border: '0',
                    padding: '0',
                    backgroundColor: 'none'
                },
                overlayCSS: {
                    backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                    opacity: options.boxed ? 0.05 : 0.1,
                    cursor: 'wait'
                }
            });
        } else { // page blocking
            $.blockUI({
                message: html,
                baseZ: options.zIndex ? options.zIndex : 1000,
                css: {
                    border: '0',
                    padding: '0',
                    backgroundColor: 'none'
                },
                overlayCSS: {
                    backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                    opacity: options.boxed ? 0.05 : 0.1,
                    cursor: 'wait'
                }
            });
        }
    },
    show: function (target, message) {
        var that = this;
        if (isNullOrBlank(message)) {
            that._blockUI({
                target: target,
                boxed: false,
                animate: false,
                textOnly: true,
                iconOnly: false,
                message: message,
                zIndex: null,
                overlayColor: null
            });
        }
        else {
            that._blockUI({
                target: target,
                boxed: false,
                animate: true,
                textOnly: false,
                iconOnly: false,
                message: null,
                zIndex: null,
                overlayColor: null
            });
        }
    },
    hide: function (target) {
        if (target) {
            $(target).unblock({
                onUnblock: function () {
                    $(target).css('position', '');
                    $(target).css('zoom', '');
                }
            });
        } else
            $.unblockUI();
    }
};


//数据提交访问错误
var handlePostJsonError = function (res) {
    if (res.status == 404) {
        //当前请求地址未找到
        S_toastr.error('当前请求地址未找到！');

    } else if (res.status == 0) {
        //网络请求超时
        S_toastr.error('网络请求超时！');
    } else {
        S_toastr.error('网络请求出现错误！status：' + res.status + "，statusText：" + res.statusText);
    }
};

//数据提交访问错误
var handleResponse = function (res) {
    var result = false;
    if (res.code === '401') {
        S_toastr.error('用户登录已超时');
        result = true;
    } else if (res.code === '500') {
        S_toastr.error('出现异常错误 !详细信息：' + res.msg);
        result = true;
    }
    return result;
};


var m_ajax = {
    get: function (option, onHttpSuccess, onHttpError) {
        $.ajax({
            type: 'GET',
            url: option.url,
            cache: false,
            beforeSend: function () {
                if (option.loadingEl)
                    S_loading.show(option.loadingEl);

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
                if (option.loadingEl)
                    S_loading.hide();

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
    getJson: function (options, onHttpSuccess, onHttpError) {
        $.ajax({
            type: 'GET',
            url: options.url,
            cache: false,
            contentType: "application/json",
            beforeSend: function () {
                if (options.loadingEl)
                    S_loading.show(options.loadingEl);

                if (options.bindDisabled) {
                    var $el = $(options.bindDisabled);
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
            success: function (res) {

                if (!handleResponse(res)) {
                    if (onHttpSuccess)
                        onHttpSuccess(res);
                }

            },
            error: function (res) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(res);
            },
            complete: function () {
                if (options.loadingEl)
                    S_loading.hide();

                if (options.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(options.bindDisabled);
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
                if (option.loadingEl)
                    S_loading.show(option.loadingEl);

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
                if (option.loadingEl)
                    S_loading.hide();

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
    postJson: function (options, onHttpSuccess, onHttpError) {
        $.ajax({
            type: 'POST',
            url: options.url,
            cache: false,
            async: options.async == null ? true : options.async,
            data: JSON.stringify(options.postData),
            contentType: "application/json",
            beforeSend: function () {
                if (options.loadingEl)
                    S_loading.show(options.loadingEl);

                if (options.bindDisabled) {
                    var $el = $(options.bindDisabled);
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
            success: function (res) {
                if (!handleResponse(res)) {
                    if (onHttpSuccess)
                        onHttpSuccess(res);
                }
            },
            error: function (res) {
                if (onHttpError)
                    onHttpError();

                handlePostJsonError(res);
            },
            complete: function () {
                if (options.loadingEl)
                    S_loading.hide();

                if (options.bindDisabled) {
                    setTimeout(function () {
                        var $el = $(options.bindDisabled);
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

