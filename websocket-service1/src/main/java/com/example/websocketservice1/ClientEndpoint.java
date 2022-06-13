package com.example.websocketservice1;

import com.example.websocketservice1.config.MessageDecoder;
import com.example.websocketservice1.config.MessageEncoder;
import com.example.websocketservice1.model.Message;

import javax.websocket.*;

import java.text.SimpleDateFormat;

import static java.lang.String.format;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ClientEndpoint extends Endpoint {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();



    @OnMessage
    public void onMessage(Message message, Session session) {
        System.out.println("[" + session.getId() + "]" +message.getName() + ": " + message.getMessage());
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println(format("Connection established. session id: %s", session.getId()));
    }
}
