package com.example.demows;

import com.alibaba.fastjson.JSONObject;

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
