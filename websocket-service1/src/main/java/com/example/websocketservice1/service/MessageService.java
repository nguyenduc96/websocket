package com.example.websocketservice1.service;

import com.example.websocketservice1.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private static List<Message> messages = new ArrayList<>();

    private static Long countP = 1L;

    public Iterable<Message> findAll() {
        return messages;
    }

    public Message save(Message Message) {
        messages.add(Message);
        countP++;
        return Message;
    }

}
