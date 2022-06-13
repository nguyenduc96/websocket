package com.example.websocketservice1.config;


import com.alibaba.fastjson.JSONObject;
import com.example.websocketservice1.model.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message>{
    @Override
    public String encode(Message message) throws EncodeException {
        return JSONObject.toJSONString(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
