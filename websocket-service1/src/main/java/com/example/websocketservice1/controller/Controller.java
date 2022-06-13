package com.example.websocketservice1.controller;

import com.example.websocketservice1.model.Message;
import com.example.websocketservice1.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;



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
    @MessageMapping("/messages/service2")
    @SendTo("/topic/messages")
    public Message createNewMessageUsingSocket(Message message) {
        System.out.println(message);
        messageService.save(message);
        return message;
    }
}
