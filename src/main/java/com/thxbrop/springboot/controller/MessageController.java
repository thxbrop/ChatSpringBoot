package com.thxbrop.springboot.controller;

import com.thxbrop.springboot.Result;
import com.thxbrop.springboot.entity.Message;
import com.thxbrop.springboot.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final MessageRepository repository;

    @Autowired
    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/message/{id}")
    public Result<Message> getById(@PathVariable("id") int id) {
        return new Result<>(repository.findById(id).orElse(null));
    }
}
