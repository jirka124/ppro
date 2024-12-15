package com.example.demo.controller;

import com.example.demo.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatsController {

    @Autowired
    public ChatsController() {}

    @GetMapping("/chats")
    public String list(Model model){
        model.addAttribute("chats", null);
        return "chats";
    }

    @GetMapping("/chat/{id}")
    public String chat(Model model, @PathVariable long id){
        // model.addAttribute("chats", null);
        return "chat";
    }
}
