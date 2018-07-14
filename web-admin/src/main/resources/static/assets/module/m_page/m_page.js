/**
 * Created by Wuwq on 2017/1/19.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_page",
        defaults = {
            loadingEl: null,
            pageIndex: 0,
            pageSize: 10,
            total: 0,
            pageBtnCount: 5,
            showFirstLastBtn: false,
            firstBtnText: null,
            lastBtnText: null,
            /*prevBtnText: "&laquo;",
             nextBtnText: "&raquo;",*/
            prevBtnText: '上一页',
            nextBtnText: '下一页',
            loadFirstPage: true,
            remote: {
                url: null,
                params: null,
                pageParams: null,
                success: null,
                beforeSend: null,
                complete: null,
                pageIndexName: 'pageIndex',
                pageSizeName: 'pageSize',
                totalName: 'data.total',
                traditional: false,
                remoteWrongFormat: null
            },
            pageElementSort: ['$page', '$size', '$jump', '$info'],
            showInfo: false,
            infoFormat: '{start} ~ {end} of {total} entires',
            noInfoText: '0 entires',
            showJump: false,
            jumpBtnText: 'Go',
            showPageSizes: false,
            pageSizeItems: [5, 10, 15, 20],
            debug: false
        };

    function Plugin(element, options) {
        this.element = element;
        var remote = $.extend({}, defaults.remote, options.remote);
        this.settings = options;
        this.settings.remote = remote;
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            if (that.settings.loadingEl !== null && that.settings.remote.beforeSend === null) {
                that.settings.remote.beforeSend = function () {
                    App.blockUI({
                        target: that.settings.loadingEl,
                        boxed: false,
                        animate: true
                    });
                }
            }
            if (that.settings.loadingEl !== null && that.settings.remote.complete === null) {
                that.settings.remote.complete = function () {
                    App.unblockUI(that.settings.loadingEl);
                }
            }

            if (that.settings.remote.remoteWrongFormat === null) {
                that.settings.remote.remoteWrongFormat = function (res) {
                    if (res && res.code === '500')
                        S_toastr.error('很抱歉，请求发生异常');
                }
            }

            if ($(that.element).pagination())
                $(that.element).pagination('destroy');
            $(that.element).pagination(that.settings);
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