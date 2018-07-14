var historyData_entry = {
    init: function () {
        var that = this;
        that._initAction();
    },
    _initAction: function () {
        $('#btnImportProjects').m_fileUploader({
            innerHTML:'导入项目',
            server: restApi.url_historyData_importProjects,
            formData: {abc:'123'},
            accept: {
                title: '请选择Excel文件',
                extensions: 'xlsx,xls',
                mimeTypes: '.xlsx,.xls'
            },
            loadingId: '',
            uploadSuccessCallback: function (file, res) {
                S_toastr.success(res.msg);
            }
        });
    }
};