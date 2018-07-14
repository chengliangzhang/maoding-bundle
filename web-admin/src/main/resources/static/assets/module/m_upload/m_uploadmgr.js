;(function ($, window, document, undefined) {
    "use strict";
    var pluginName = "m_uploadmgr",
        defaults = {
            server: null,
            auto: false,//选择文件后是否自动开始上传
            chunked: true,//是否分块上传，需要后台配合
            chunkSize: 5 * 1024 * 1024,
            chunkRetry: 3,
            /*fileExts: 'pdf,zip,rar,gif,jpg,jpeg,bmp,png',*/
            fileExts: '*',
            fileSingleSizeLimit: null,
            btnPickId: null,
            btnPickText: '上传',
            formData: {},
            closeIfFinished: false,
            uploadBeforeSend: null,
            beforeFileQueued: null,
            uploadSuccessCallback: null
        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = options;

        this._defaults = defaults;
        this._name = pluginName;
        this._uploader = null;
        this.init();
    }

    // Avoid Plugin.prototype conflicts
    $.extend(Plugin.prototype, {
        init: function () {
            var that = this;
            that._initWebUploader();
        },
        _initWebUploader: function () {
            var that = this;

            var html = template('m_docmgr/m_uploadmgr', {});
            $(that.element).html(html);

            that._uploader = WebUploader.create({
                fileSingleSizeLimit: that.settings.fileSingleSizeLimit,
                compress: false,// 不压缩image
                auto: that.settings.auto,
                swf: window.rootPath + '/assets2/lib/webuploader/Uploader.swf',
                server: that.settings.server,
                //timeout: 600000,
                pick: {
                    id: '.btn-select:eq(0)',
                    innerHTML: null,
                    multiple: true
                },
                duplicate: false,//是否可重复选择同一文件
                resize: false,
                chunked: that.settings.chunked,
                chunkSize: that.settings.chunkSize,
                chunkRetry: that.settings.chunkRetry,
                formData: that.settings.formData,
                accept: that.settings.accept || {
                    extensions: that.settings.fileExts
                },
                threads: 1,
                disableGlobalDnd: true
            });

            //文件队列
            that._uploader.on('beforeFileQueued', function (file) {
                if (_.isBlank(file.ext)) {
                    that._alertError(file.name + ' 缺少扩展名，无法加入上传队列');
                    return false;
                }

                /* if(getStringLength(file.name)>42)
                 {
                 that._alertError(file.name+' 文件名超出长度限制');
                 return false;
                 }*/

                if (that._uploader.isInProgress()) {
                    that._alertError('当前正在上传，禁止添加新文件到队列中');
                    return false;
                }


                if (that.settings.beforeFileQueued && typeof that.settings.beforeFileQueued === 'function') {
                    if (that.settings.beforeFileQueued(file, that) === false)
                        return false;
                }


                //that._uploader.reset();//单个上传重置队列，防止队列不断增大
                return true;
            });
            that._uploader.on('filesQueued', function (files) {
                /* that._uploader.option("formData", {
                 uploadId: WebUploader.Base.guid()
                 });*/

                if (files && files.length > 0) {
                    $.each(files, function (index, file) {
                        var html = template('m_docmgr/m_uploadmgr_uploadItem', {file: file});
                        $(that.element).find('.upload-item-list:eq(0)').append(html);

                        var $uploadItem = $(that.element).find('.uploadItem_' + file.id + ':eq(0)');
                        $uploadItem.find('.span_status:eq(0)').html('待上传');

                        $uploadItem.find('.removefile:eq(0)').click(function () {
                            that._uploader.removeFile($uploadItem.attr('data-fileId'), true);
                            $uploadItem.remove();
                            return false;
                        });
                    });
                    $(that.element).find('.btn-start:eq(0)').show();
                }
            });
            that._uploader.on('startUpload', function (file) {
                $(that.element).find('.btn-start:eq(0)').hide();
                $(that.element).find('.btn-stop:eq(0)').show();
            });
            that._uploader.on('uploadStart', function (file) {
                var $uploadItem = $(that.element).find('.uploadItem_' + file.id + ':eq(0)');
                $uploadItem.find('.span_status:eq(0)').html('正在上传');
            });
            //进度
            that._uploader.on('uploadProgress', function (file, percentage) {
                var pc = (percentage * 100).toFixed(2);
                if (percentage >= 1)
                    pc = 100;
                var $uploadItem = $(that.element).find('.uploadItem_' + file.id + ':eq(0)');
                $uploadItem.find('.span_progress:eq(0)').html(pc + '%');
                $uploadItem.find('.progress-bar:eq(0)').attr('aria-valuenow', pc).css('width', pc + '%')
            });
            //当某个文件的分块在发送前触发，主要用来询问是否要添加附带参数，大文件在开起分片上传的前提下此事件可能会触发多次
            that._uploader.on("uploadBeforeSend", function (object, data, headers) {
                if (that.settings.chunked === true)
                    data.chunkPerSize = that.settings.chunkSize;
                if (that.settings.uploadBeforeSend)
                    that.settings.uploadBeforeSend(object, data, headers);
            });
            //当某个文件上传到服务端响应后，会派送此事件来询问服务端响应是否有效。
            that._uploader.on("uploadAccept", function (object, response) {
                if (!handleResponse(response)) {
                    if (response.code) {
                        if (response.code === '0' && response.data) {
                            //分片后续处理
                            if (response.data.needFlow === true) {
                                that._uploader.options.formData.fastdfsGroup = response.data.fastdfsGroup;
                                that._uploader.options.formData.fastdfsPath = response.data.fastdfsPath;
                            }
                            return true;
                        } else {
                            if (object && object.file && object.file.name)
                                that._alertError(object.file.name + " 上传失败(#01)，" + response.msg);
                            else
                                that._alertError("上传失败(#02)，" + response.msg);
                        }
                    }
                }
                return false;
            });
            //上传成功
            that._uploader.on('uploadSuccess', function (file, res) {
                //还要判断response
                if (!handleResponse(res)) {
                    var $uploadItem = $(that.element).find('.uploadItem_' + file.id + ':eq(0)');
                    if (res.code == 0) {
                        $uploadItem.find('.span_status:eq(0)').html('上传成功');
                        that._uploader.removeFile(file, true);
                        if (that.settings.uploadSuccessCallback)
                            that.settings.uploadSuccessCallback(file, res);
                    } else if (res.code === '1') {
                        S_dialog.error(res.msg);
                        $uploadItem.find('.span_status:eq(0)').html('上传失败');
                    } else {
                        $uploadItem.find('.span_status:eq(0)').html('上传失败');
                    }
                }

            });
            //当所有文件上传结束时触发
            that._uploader.on("uploadFinished", function () {
                $(that.element).find('.btn-start:eq(0)').hide();
                $(that.element).find('.btn-stop:eq(0)').hide();
                if (that.settings.closeIfFinished) {
                    setTimeout(function () {
                        $(that.element).find('a.btn-close').click();

                    }, 500);
                }
            });
            //上传失败
            that._uploader.on('uploadError', function (file, reason) {
                var $uploadItem = $(that.element).find('.uploadItem_' + file.id + ':eq(0)');
                if ($uploadItem.length > 0) {
                    if (!!reason) {
                        //that._alertError(file.name + ' 上传失败（' + reason + '）');
                        $uploadItem.find('.span_status:eq(0)').html('上传失败（' + reason + '）');
                    }
                    else {
                        //that._alertError(file.name + ' 上传失败');
                        $uploadItem.find('.span_status:eq(0)').html('上传失败');
                    }
                }
            });
            that._uploader.on('error', function (handler) {
                var content;
                switch (handler) {
                    case 'F_EXCEED_SIZE':
                        content = '文件大小超出范围';
                        break;
                    case 'Q_EXCEED_NUM_LIMIT':
                        content = '已超最大的文件上传数';
                        break;
                    case 'Q_TYPE_DENIED':
                        content = '仅支持上传如下类型文件：' + that.settings.fileExts;
                        break;
                    case 'F_DUPLICATE':
                        content = '文件已经添加';
                        break;
                    default:
                        content = '文件添加失败';
                        break;
                }

                that._alertError(content);
            });

            that._bindAction();
        },
        //绑定按钮
        _bindAction: function () {
            var that = this;
            //开始
            $(that.element).find('.btn-start:eq(0)').click(function () {

                var start = function () {
                    var server = that._uploader.option('server');
                    if (server === null) {
                        that._alertError('上传路径没有正确配置');
                        return false;
                    }

                    var files = that._uploader.getFiles();
                    if (!files || files.length == 0) {
                        that._alertError('请先选择要上传的文件');
                        return false;
                    }

                    var errorFiles = [];
                    var interruptFiles = [];
                    $.each(files, function (index, file) {
                        var fileStatus = file.getStatus();
                        if (fileStatus === 'error') {
                            errorFiles.push(file);
                            //that._uploader.retry(file);
                            //console.log("error:"+file.name);
                        } else if (fileStatus === 'interrupt') {
                            interruptFiles.push(file);
                            //that._uploader.retry(file);
                            //console.log("interrupt:"+file.name);
                        }

                        //console.log(fileStatus);
                    });

                    if (errorFiles.length + interruptFiles.length > 0) {
                        $.each(interruptFiles, function (index, file) {
                            file.setStatus('error');
                        });
                        that._uploader.retry();
                    }
                    else
                        that._uploader.upload();
                };


                var option = {
                    ignoreError: true,
                    url: restApi.url_getCompanyDiskInfo,
                    postData: {
                        companyId: window.currentCompanyId
                    }
                };
                m_ajax.postJson(option, function (res) {
                    if (res.code === '0') {
                        var freeSize = parseFloat(res.data.freeSize);
                        if (freeSize <= 0) {
                            S_toastr.warning("当前组织网盘空间不足，无法上传，请联系客服")
                        } else {
                            start();
                        }
                    }
                });


                return false;
            });

            //停止
            $(that.element).find('.btn-stop:eq(0)').click(function () {
                var files = that._uploader.getFiles();
                if (files && files.length > 0) {
                    $.each(files, function (index, file) {
                        if (file.getStatus() === 'progress');
                        {
                            try {
                                that._uploader.stop(file);
                            } catch (ex) {

                            }
                            if (file.getStatus() === 'interrupt') {
                                var $uploadItem = $(that.element).find('.uploadItem_' + file.id + ':eq(0)');
                                $uploadItem.find('.span_status:eq(0)').html('已中断');
                            }
                            //file.setStatus('error');
                        }
                        //console.log(file.getStatus());
                    });
                }
                $(that.element).find('.btn-stop:eq(0)').hide();
                $(that.element).find('.btn-start:eq(0)').show();
            });

            //关闭
            $(that.element).find('.btn-close:eq(0)').click(function () {
                if (that._uploader.isInProgress()) {
                    that._alertError("当前正在上传，无法关闭")
                } else {
                    var files = that._uploader.getFiles();
                    if (files && files.length > 0) {
                        $.each(function (index, file) {
                            that._uploader.removeFile(file, true);
                        });
                    }
                    that._uploader.destroy();
                    $(that.element).html('');
                }
                return false;
            });
        },
        //暂时未用
        _onError: function (file, msg) {
            //showMsg
            //为了可以重试，设置为错误状态
            file.setStatus('error');
            var that = this;
            that._alertError(file.name + ' ' + msg);
        },
        //错误提示
        _alertError: function (content, alertId) {
            var that = this;
            var html = template('m_alert/m_alert_error', {content: content, id: alertId});
            $(that.element).find('.alertmgr:eq(0)').append(html);
        },
        //获取WebUploader实例
        getUploader: function () {
            var that = this;
            return that._uploader;
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
