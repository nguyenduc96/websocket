package com.example.websocketservice1.config;


import com.alibaba.fastjson.JSONObject;
import com.example.websocketservice1.model.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message>{
    @Override
    public Message decode(String s) throws DecodeException {
        Message message = JSONObject.parseObject(s, Message.class);
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
