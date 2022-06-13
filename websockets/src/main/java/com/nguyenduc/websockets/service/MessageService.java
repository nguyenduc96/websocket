package com.nguyenduc.websockets.service;

import com.nguyenduc.websockets.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
