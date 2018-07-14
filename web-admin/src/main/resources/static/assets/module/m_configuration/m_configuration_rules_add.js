/**
 * 规则配置
 * Created by wrb on 2018/04/18.
 */
;(function ($, window, document, undefined) {

    "use strict";
    var pluginName = "m_configuration_rules_add",
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
            that.render();
        }
        ,render: function () {
            var that = this;
            that.getRuleList(function (data) {

                var html = template('m_configuration/m_configuration_rules_add', {ruleList:data});
                $(that.element).html(html);
                $(that.element).find('select[name="ruleType"]').select2({
                    allowClear: false,
                    language: "zh-CN",
                    minimumResultsForSearch: -1
                });
                that.bindActionClick();
                that.bindRuleChange();
                if(data!=null && data.length>0){
                    var defaultSelectVal = $('select[name="ruleType"]>option:nth-child(2)').val();
                    $(that.element).find('select[name="ruleType"]').val(defaultSelectVal).trigger('change');
                }
            });
        }
        //查询父菜单
        ,getRuleList:function (callback) {
            var that = this;
            var option = {};
            option.url=restApi.url_rule_getRuleType;
            m_ajax.getJson(option, function (res) {
                console.log(res);
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
        ,saveRule:function () {
            var that = this;
            var valueList = [];
            $(that.element).find('input[name="candidates"]:checked').each(function () {
                var $this = $(this);
                var ruleValueList = [];
                $this.closest('td').next().find('input[name="candidatesItem"]:checked').each(function () {
                    ruleValueList.push($(this).attr('data-id'));
                });
                var valueObj = {
                    codeType:$this.attr('data-value'),
                    ruleValueList:ruleValueList
                };
                valueList.push(valueObj);
            });
            var option = {
                url: restApi.url_rule_saveRule,
                postData: {
                    name:$(that.element).find('input[name="name"]').val(),
                    ruleType:$(that.element).find('select[name="ruleType"]').val(),
                    valueList:valueList

                }
            };
            console.log(option)
            m_ajax.postJson(option, function (res) {
                if (res.code === '0') {
                    S_toastr.success('提交成功');
                } else {
                    S_toastr.error(res.msg);
                }
            });
        }

        //规则select事件
        ,bindRuleChange:function () {
            var that = this;
            $(that.element).find('select[name="ruleType"]').on('change',function () {

                var $this = $(this);
                var ruleType = $this.val();
                var option = {};
                option.url=restApi.url_rule_getCandidateType+'/'+ruleType;
                m_ajax.getJson(option, function (res) {
                    if (res.code === '0') {

                        var html = template('m_configuration/m_configuration_rules_candidates', {candidatesList:res.data});
                        $(that.element).find('#candidatesBox').html(html);
                        that.initCandidatesICheck();

                    } else {
                        S_toastr.error(res.msg);
                    }
                });

            });
        }
        //初始ICheck
        ,initCandidatesICheck:function () {
            var that = this;
            var ifChecked = function (e) {
            };
            var ifUnchecked = function (e) {
            };
            $(that.element).find('input[name="candidates"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifUnchecked).on('ifChecked.s', ifChecked);
            var ifItemChecked = function (e) {
                $(this).closest('td').prev().find('input[name="candidates"]').prop('checked',true);
                $(this).closest('td').prev().find('input[name="candidates"]').iCheck('update');
            };
            var ifItemUnchecked = function (e) {
                var len = $(this).closest('td').find('input[name="candidatesItem"]:checked').length;
                if(len==0){
                    $(this).closest('td').prev().find('input[name="candidates"]').prop('checked',false);
                    $(this).closest('td').prev().find('input[name="candidates"]').iCheck('update');
                }
            };
            $(that.element).find('input[name="candidatesItem"]').iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            }).on('ifUnchecked.s', ifItemUnchecked).on('ifChecked.s', ifItemChecked);
        }
        //事件绑定
        ,bindActionClick:function () {
            var that = this;
            $(that.element).find('button[data-action]').off('click').on('click',function () {
                var $this = $(this);
                var dataAction = $this.attr('data-action');

                switch (dataAction){
                    case 'save':
                        that.saveRule();
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