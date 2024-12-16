package com.example.demo.controller;

import com.example.demo.model.Reaction;
import com.example.demo.model.User;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReactionController {

    private ReactionService reactionService;
    private UserService userService;

    @Autowired
    public ReactionController(ReactionService reactionService, UserService userService) {
        this.reactionService = reactionService;
        this.userService = userService;
    }

    @GetMapping("/reactions/react")
    public String list(@RequestParam long to, @RequestParam int type) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUserDetails loggedUser;
        User fromUser;
        User toUser;

        if (principal instanceof MyUserDetails) loggedUser = ((MyUserDetails) principal);
        else throw new Exception("User not logged");

        toUser = userService.getUserById(to);
        if(toUser == null)  throw new Exception("Unknown reaction obtainer");

        fromUser = loggedUser.getUser();

        /*
        System.out.println("Logged in user: " + loggedUser.getUsername());
        System.out.println("Reacted to user: " + toUser.getUsername());
        System.out.println("REACTION: " + type);
        */

        Reaction reaction = new Reaction();
        reaction.setFromUser(fromUser);
        reaction.setToUser(toUser);
        reaction.setType(type);
        reactionService.saveReaction(reaction);

        return "redirect:/";
    }
}
