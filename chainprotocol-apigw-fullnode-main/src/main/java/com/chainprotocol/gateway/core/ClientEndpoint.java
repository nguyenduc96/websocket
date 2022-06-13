//package com.chainprotocol.gateway.core;
//
//
//import com.chainprotocol.gateway.core.config.MessageEncoder;
//import com.chainprotocol.gateway.model.Message;
//
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import java.text.SimpleDateFormat;
//
//import static java.lang.String.format;
//
//@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)
//public class ClientEndpoint {
//
//    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
//
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println(format("Connection established. session id: %s", session.getId()));
//    }
//
//    @OnMessage
//    public void onMessage(Message message, Session session) {
//        System.out.println("[" + session.getId() + "]" +message.getName() + ": " + message.getMessage());
//    }
//
//}
