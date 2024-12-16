package com.example.demo.service;

import com.example.demo.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {

    List<Message> getAllMessages();
    List<Message> getMessagesByIds(long userId1, long userId2);
    void deleteMessageById(long id);
    void saveMessage(Message message);
}
