/**
 * Created by Wuwq on 2016/10/12.
 */
var sockClient={
    eventBus:null,
    endPointUrl: null,
    clientAuthUrl:null,
    clientSubUrl_topicAll: null,
    clientSubUrl_topicSpec: null,
    userId:null,
    userToken: null,
    reconnectTime: 0,
    init: function (userId,userToken) {
        var root=sockClient;
        root.userId = userId;
        root.userToken=userToken;
        root.endPointUrl='http://127.0.0.1:8383/sockjs';
        root.clientAuthUrl='clientAuthUrl';
        //root.clientSubUrl_topicAll='clientSubUrl.topicAll';
        root.clientSubUrl_topicSpec='clientSubUrl.topicSpec.dfg123456456';
    },
    connect: function () {
        var root=sockClient;
        root.eventBus=new EventBus(root.endPointUrl);
        root.eventBus.onopen = function() {
            var authHeader={userId:root.userId,userToken:root.userToken};
            root.eventBus.registerHandler(root.clientSubUrl_topicSpec,authHeader, function(error, message) {
                console.log(error);
                console.log(message);
                $('#content').append('<p>[全体消息]: ' + JSON.stringify(message)+'</p>');
                //确认收到
                root.eventBus.send(message.replyAddress);
            });

            /*root.eventBus.registerHandler(root.clientSubUrl_topicAll, function(error, message) {
                $('#content').append('<p>[全体消息]: ' + JSON.stringify(message)+'</p>');
            });*/
            // root.eventBus.registerHandler(root.clientSubUrl_topicSpec, authHeader,function(error, message) {
            //     console.log(error);
            //     console.log(message);
            //     $('#content').append('<p>[个人]: ' + JSON.stringify(message)+'</p>');
            //     //确认收到
            //     root.eventBus.send(message.replyAddress);
            // });
            /*root.eventBus.login(root.userId,root.userToken, function(res) {
                 if(res.isOk&&res.isOk===true){
                     root.eventBus.onclose = function(){

                     };

                 }
            });*/
        };
    },
    disconnect: function () {
        var root=sockClient;
        if(root.eventBus != null)
            root.eventBus.close();
    },
    sendAll: function (content) {
        var root=sockClient;
        var serverSubUrl_topicAll='serverSubUrl_topicAll';
        console.log(content);
        root.eventBus.publish(serverSubUrl_topicAll, {content:content});
    },
    sendSpec: function (userId, content) {
        var root=sockClient;
        var serverSubUrl_topicSpec='serverSubUrl_topicSpec/'+userId;
        root.eventBus.publish(serverSubUrl_topicSpec, {content:content});
    }
};