/**
 * Created by wrb on 2018/04/18.
 */
var configuration_menuSet = {
    init: function () {
        var that = this;
        $('.page-content-wrapper .portlet-body').m_configuration_menu_list({},true);
        that.bindAction();
    }
    ,bindAction:function () {
        var that = this;
        $('.page-content-wrapper').find('button[data-action]').off('click').on('click',function () {

            var $this = $(this);
            var dataAction = $this.attr('data-action');
            switch (dataAction){
                case 'addMenu':

                    $('.page-content-wrapper .portlet-body').m_configuration_menu_add({},true);

                    break;
                case 'addOperation':

                    break;
            }

        });
    }
};