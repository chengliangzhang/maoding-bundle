/**
 * Created by wrb on 2018/04/18.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_configuration_operation_add",
        defaults = {
            $belongingView:null//所属菜单
        };

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
            that.render();
        }
        ,render: function () {
            var that = this;
            console.log(1111111111)
            var html = template('m_configuration/m_configuration_operation_add', {belongingView:that.settings.$belongingView});
            $(that.element).html(html);
            that.bindActionClick();
        }
        //保存菜单
        ,getParentViewList:function (callback) {
            var that = this;
            var option = {};
            option.url=restApi.url_configuration_getParentViewList;
            m_ajax.getJson(option, function (res) {
                if (res.code === '0') {
                    if(callback!=null){
                        callback(res.data);
                    }
                } else {
                    S_toastr.error(res.msg);
                }
            });
        }
        //保存菜单
        ,saveMenu:function () {
            var that = this;
            var $data = $("form.m_configuration_operation_add").serializeObject();
            $data.viewId = that.settings.$belongingView.id;
            $data.viewCode = that.settings.$belongingView.viewCode;
            var option = {
                url: restApi.url_configuration_saveOperatePermission,
                postData: $data
            };
            console.log(option)
            m_ajax.postJson(option, function (res) {
                if (res.code === '0') {
                    S_toastr.success('提交成功');
                    that.back();
                } else {
                    S_toastr.error(res.msg);
                }
            });
        }
        //返回列表
        ,back:function () {
            $('.page-content-wrapper .portlet-body').m_configuration_menu_list({},true);
        }
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'save':
                        that.saveMenu();
                        return false;
                        break;
                    case 'back':
                        that.back();
                        return false;
                        break;
                }
            })
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