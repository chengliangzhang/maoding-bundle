/**
 * Created by Wuwq on 2017/07/12.
 */
var main = {
    init: function (pageJs) {
        var that = this;

        that._initSelect2();

        that._initSidebar();

        that._initPageJs(pageJs);
    },
    _initSidebar: function () {
        var $sidebarWrapper = $('.page-sidebar-wrapper');
        if ($sidebarWrapper.length > 0)
            $sidebarWrapper.m_sidebar();
    },
    _initPageJs: function (pageJs) {
        if (pageJs && pageJs.init)
            pageJs.init();
    },
    _initSelect2: function () {
        if ($.fn.select2)
            $.fn.select2.defaults.set("theme", "bootstrap");
    }
};
