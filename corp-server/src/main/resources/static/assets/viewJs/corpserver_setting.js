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