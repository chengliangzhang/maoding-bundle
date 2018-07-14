package com.maoding.notify.verticle;


import com.maoding.notify.config.AppConfig;
import com.maoding.utils.SpringContextUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.sockjs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Wuwq on 2016/10/11.
 */
@Component
public class SockJsServerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(SockJsServerVerticle.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private CorsHandler corsHandler;

    @Override
    public void start() throws Exception {

        try{
            Router router = Router.router(vertx);

            SockJSHandlerOptions sockJSHandlerOptions = new SockJSHandlerOptions();
            sockJSHandlerOptions.setHeartbeatInterval(1000);

            SockJSHandler handler = SockJSHandler.create(vertx, sockJSHandlerOptions);

            //访问权限
            BridgeOptions bridgeOptions = new BridgeOptions()
                    .addOutboundPermitted(new PermittedOptions().setAddressRegex("clientSubUrl.topicSpec.*"));

            //处理事件
            Handler<BridgeEvent> bridgeEventHandler = event -> {
            /*BridgeEventType bridgeEventType=event.type();
            if(bridgeEventType==BridgeEventType.SOCKET_CREATED)
            {

            }*/


//
//            logger.debug("bridgeEventType:{}",bridgeEventType);
//            if (bridgeEventType == BridgeEventType.REGISTER) {
//                Map<String, Object> msgMap=event.getRawMessage().getMap();
//                String replyAddress = (String)msgMap.get("replyAddress");
//                String address = (String)msgMap.get("address");
//                logger.debug("address:{} replyAddress:{}",address,replyAddress);
//                SockJSSocket socket=event.socket();
//                String userId=socket.headers().get(SessionUtils.Key_UserId);
//                String userToken=socket.headers().get(SessionUtils.Key_UserToken);
//                logger.debug("userId:{}  userToken:{}",userId,userToken);
//                //vertx.eventBus().publish(address,new JsonObject().put("message", "Found New Register"));
////                if(AuthUtils.validated(userId,userToken)){
////                    SessionUtils.add(userId,socket);
////                }else{
////                    socket.close();
////                }
//            }

                event.complete(true);
            };

            handler.bridge(bridgeOptions, bridgeEventHandler);

            //处理跨域问题
            router.route().handler(corsHandler);

            router.route(appConfig.getSockJsEndPoint()).blockingHandler(handler);

            HttpServerOptions opts = new HttpServerOptions();
            opts.setHost(appConfig.getSockJsHost());
            opts.setPort(appConfig.getSockJsPort());
            vertx.createHttpServer(opts).requestHandler(router::accept).listen();

            ActiveMQVerticleForWeb verticleForWeb = SpringContextUtils.getApplicationContext().getBean(ActiveMQVerticleForWeb.class);
            vertx.deployVerticle(verticleForWeb);

            ActiveMQVerticleForApp verticleForApp = SpringContextUtils.getApplicationContext().getBean(ActiveMQVerticleForApp.class);
            vertx.deployVerticle(verticleForApp);
        }catch (Exception e){
            logger.error("SockJsServerVerticle异常:{}", e);
        }

    }
}
