/**
 * Created by Wuwq on 2017/05/26.
 */
var login = {
    init: function () {
        var that = this;
        that._bindAction();
        that._bindKeyDownEnter();

        $.backstretch([
            window.rootPath + '/assets/img/bg/1.jpg',
            window.rootPath + '/assets/img/bg/2.jpg',
            window.rootPath + '/assets/img/bg/3.jpg'
        ], {
            fade: 1000,
            duration: 5000
        });
    },
    _bindAction: function () {
        var that = this;
        $('#btnLogin').click(function () {
            that._login();
        });
    },
    _bindKeyDownEnter: function () {
        var that = this;
        $('#account').keydown(function (e) {
            if (event.keyCode == 13) {
                that._login();
                stopPropagation(e);
                preventDefault(e);
                return false;
            }
        });
        $('#password').keydown(function (e) {
            if (event.keyCode == 13) {
                that._login();
                stopPropagation(e);
                preventDefault(e);
                return false;
            }
        });
    },
    _login: function () {
        var account = $('#account').val();
        var password = $('#password').val();
        if (isNullOrBlank(account) || isNullOrBlank(password)) {
            S_toastr.warning('账号或密码不能为空');
            return;
        }

        var option = {
            url: restApi.url_loginSubmit,
            loadingEl: '',
            postData: {
                account: account,
                password: password
            }
        };
        m_ajax.postJson(option, function (res) {
            if (res.code === '0') {
                S_toastr.success('登陆成功，正在跳转...');
                setTimeout(function () {
                    window.location.href = window.rootPath + '/orgAuth/approveList';
                },1000);
            } else {
                S_toastr.error(res.msg);
            }
        });
    }
};