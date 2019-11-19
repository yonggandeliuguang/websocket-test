package com.yang.controller;

import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
@ServerEndpoint(value = "/websocket/{id}")
@Controller
public class WebsocketController {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    public WebsocketController() {
    }
    // 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static Map<String,Session> sessionMap = new HashMap<String,Session>();

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id){
        sessionMap.put(id,session);     //加入map中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！id为"+id+"，当前在线人数为" + getOnlineCount());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("id")String id){
        sessionMap.remove(id);  //从map中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }
    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @param id   用户id标识
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("id")String id) {
        System.out.println("来自客户端"+id+"的消息:" + message);
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /**
     * 向客户端发送消息
     * @param message	要发送的消息
     * @param id   用户id标识
     * @throws IOException
     */
    public void sendMessage(String id,String message) throws IOException {
        Session session = sessionMap.get(id);	//获得当前用户的session
        session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebsocketController.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebsocketController.onlineCount--;
    }
}
