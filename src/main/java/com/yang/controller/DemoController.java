package com.yang.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private WebsocketController webSocketController;

    /**
     * 调用服务端向客户端发送消息
     * @param id 用户id标识
     * @return
     */
    @RequestMapping("/message")
    public String sendMessage(String id) throws IOException {
        String message = id+"调用服务端向客户端发送消息成功！";
        webSocketController.sendMessage(id,message);
        return "success";
    }

    /**
     * 调用服务端向客户端发送json数据
     * @param id 用户id标识
     * @return
     */
    @RequestMapping("/json")
    public String sendJson(String id) throws IOException {
        Map<String,String> map = new HashMap<String,String>();
        map.put("name","张三");
        map.put("age","22");
        String json = JSON.toJSONString(map);
        webSocketController.sendMessage(id,json);
        return "success";
    }
}


