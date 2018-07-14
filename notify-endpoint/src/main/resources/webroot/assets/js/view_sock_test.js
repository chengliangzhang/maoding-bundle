var view_sock_test = {
    init: function () {
        var root = view_sock_test;
        root.bindBtnSendAll();
        root.bindBtnSendSpec();

        var id=root.randomId(100);
        var token='xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (a, b) {
            return b = Math.random() * 16, (a == 'y' ? b & 3 | 8 : b | 0).toString(16);
        });
       /* console.log(id);
        console.log(token);*/
        $('#userId').val('dfg123456456');
        $('#userToken').val('654464');

        sockClient.init('dfg123456456','654464');
        sockClient.connect();
    },
    randomId:function(max){
        return parseInt(Math.random()*max+1);
    },
    bindBtnSendAll: function () {
        $('#btnSendAll').click(function () {
            var msg = $('#inputMsg').val();
            sockClient.sendAll(msg);
        });
    },
    bindBtnSendSpec: function () {
        $('#btnSendSpec').click(function () {
            var targetId=$('#targetId').val();
            var msg = $('#inputMsg').val();
            sockClient.sendSpec(targetId,msg);
        });
    }
};