/**
 * 规则配置
 * Created by wrb on 2018/04/18.
 */
var configuration_rulesSet = {
    init: function () {
        var that = this;
        var option = {};
        $('.page-content-wrapper .portlet-body').m_configuration_rules_add(option,true);
        that.bindAction();
    }
    ,bindAction:function () {
        var that = this;
        $('.page-content-wrapper').find('button[data-action]').off('click').on('click',function () {

            var $this = $(this);
            var dataAction = $this.attr('data-action');
            switch (dataAction){

            }

        });
    }
};