package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.model.Reaction;
import com.example.demo.model.User;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class ChatsController {

    private UserService userService;
    private ReactionService reactionService;

    @Autowired
    public ChatsController(UserService userService, ReactionService reactionService) {
        this.userService = userService;
        this.reactionService = reactionService;
    }

    @GetMapping("/chats")
    public String list(Model model) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUserDetails loggedUser;
        User fromUser;

        if (principal instanceof MyUserDetails) loggedUser = ((MyUserDetails) principal);
        else throw new Exception("User not logged");

        fromUser = loggedUser.getUser();
        List<User> users = userService.getAllUsers();
        List<Reaction> fromReactions = reactionService.getAllFromReactions(fromUser.getId());
        List<Reaction> toReactions = reactionService.getAllToReactions(fromUser.getId());

        List<Reaction> allReactions = new ArrayList<>();
        allReactions.addAll(fromReactions);
        allReactions.addAll(toReactions);

        List<User> chatUsers = new ArrayList<>();

        for(User user : users) {
            boolean isChatable = false;

            for(Reaction r : fromReactions) {
                if(r.getType() == 2) {
                    // must be present on both sides
                    for(Reaction rr : toReactions) {
                        if(rr.getFromUser().getId().equals(user.getId()) && r.getType() > 1) isChatable = true;
                    }
                }
                else if(r.getType() == 3) {
                    // can be one way
                    if(r.getToUser().getId().equals(user.getId())) isChatable = true;
                }
            }

            for(Reaction r : toReactions) {
                if(r.getType() == 2) {
                    // must be present on both sides
                    for(Reaction rr : fromReactions) {
                        if(rr.getToUser().getId().equals(user.getId()) && r.getType() > 1) isChatable = true;
                    }
                }
                else if(r.getType() == 3) {
                    // can be one way
                    if(r.getFromUser().getId().equals(user.getId())) isChatable = true;
                }
            }

            if(isChatable) chatUsers.add(user);
        }

        model.addAttribute("chatUsers", chatUsers);
        model.addAttribute("chatUsersCount", chatUsers.size());
        return "chats";
    }

    @GetMapping("/chat/{id}")
    public String chat(Model model, @PathVariable long id){
        // model.addAttribute("chats", null);
        return "chat";
    }
}
