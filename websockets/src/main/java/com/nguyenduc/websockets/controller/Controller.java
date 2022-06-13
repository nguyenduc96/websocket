package com.nguyenduc.websockets.controller;

import com.nguyenduc.websockets.model.Message;
import com.nguyenduc.websockets.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
@CrossOrigin("*")
@RequestMapping("api/messages")
public class Controller {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<Iterable<Message>> getAllMessage() {
        return new ResponseEntity<>(messageService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return new ResponseEntity<>(messageService.save(message), HttpStatus.OK);
    }

    //Websocket
    @MessageMapping("/messages/service1")
    @SendTo("/topic/messages")
    public Message createNewMessageUsingSocket(Message message) throws ExecutionException, InterruptedException, TimeoutException {
        return messageService.save(message);
    }
}
