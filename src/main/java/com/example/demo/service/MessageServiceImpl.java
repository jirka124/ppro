package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService{

    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getMessagesByIds(long userId1, long userId2) {
        List<Message> all = new ArrayList<>();

        all.addAll(messageRepository.findByFromUserIdAndToUserId(userId1, userId2));
        all.addAll(messageRepository.findByFromUserIdAndToUserId(userId2, userId1));

        Collections.sort(all, new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return Long.compare(m1.getId(), m2.getId());
            }
        });

        return all;
    }

    @Override
    public void deleteMessageById(long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }
}
