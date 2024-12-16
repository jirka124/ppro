package com.example.demo.controller;

import com.example.demo.model.Reaction;
import com.example.demo.model.User;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class MainController {

    private UserService userService;
    private ReactionService reactionService;

    @Autowired
    public MainController(UserService userService, ReactionService reactionService) {
        this.userService = userService;
        this.reactionService = reactionService;
    }

    @GetMapping("/")
    public String main(Model model) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUserDetails loggedUser;
        User fromUser;

        if (principal instanceof MyUserDetails) loggedUser = ((MyUserDetails) principal);
        else throw new Exception("User not logged");

        fromUser = loggedUser.getUser();
        List<User> users = userService.getAllUsers();
        List<Reaction> pastReactions = reactionService.getAllFromReactions(fromUser.getId());

        List<User> freshUsers = new ArrayList<>();

        for(User user : users) {
            boolean isFresh = true;

            // do not show itself
            if(user.getId().equals(fromUser.getId())) isFresh = false;

            // do not show profiles already reacted to
            for(Reaction r : pastReactions) {
                if(r.getToUser().getId().equals(user.getId())) isFresh = false;
            }

            if(isFresh) freshUsers.add(user);
        }

        User swipeUser;
        if(!freshUsers.isEmpty()) swipeUser = freshUsers.get(new Random().nextInt(freshUsers.size()));
        else swipeUser = null;

        model.addAttribute("swipeUser", swipeUser);
        return "main";
    }

    @GetMapping("/403")
    @ResponseBody
    public String forbidden(){
        return "<h1>Access Denied</h1>";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "<h1>Admin Section</h1>";
    }

    @GetMapping("/blog")
    @ResponseBody
    public String blog(){
        return "<h1>Blog Content</h1>";
    }

}
