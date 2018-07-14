/**
 * Created by wrb on 2018/04/18.
 */
var configuration_roleViews = {
    init: function () {
        var that = this;
        var option = {};
        option.$renderCallBack = function (data,ele) {
            if(data!=null && data.length>0){
                $(ele).find('a[data-action="getViewsByRole"]:first').click();
            }
        };
        $('.page-content-wrapper #role-box').m_configuration_role(option,true);
        that.bindAction();
    }
    ,bindAction:function () {
        var that = this;
        $('.page-content-wrapper').find('button[data-action]').off('click').on('click',function () {

            var $this = $(this);
            var dataAction = $this.attr('data-action');
            switch (dataAction){
                case 'addMenu':


                    break;
                case 'addOperation':

                    break;
            }

        });
    }
};