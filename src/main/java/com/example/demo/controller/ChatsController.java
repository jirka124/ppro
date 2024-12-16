package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.MessageService;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class ChatsController {

    private UserService userService;
    private ReactionService reactionService;
    private MessageService messageService;

    @Autowired
    public ChatsController(UserService userService, ReactionService reactionService, MessageService messageService) {
        this.userService = userService;
        this.reactionService = reactionService;
        this.messageService = messageService;
    }

    private List<User> getChats(Long userId, List<User> users, List<Reaction> reactionsFromUser, List<Reaction> reactionsToUser) {
        // TODO
        return null;
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
    public String chat(Model model, @PathVariable long id) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUserDetails loggedUser;
        User fromUser;
        User toUser;

        if (principal instanceof MyUserDetails) loggedUser = ((MyUserDetails) principal);
        else throw new Exception("User not logged");

        toUser = userService.getUserById(id);
        if(toUser == null)  throw new Exception("Unknown chat user");

        fromUser = loggedUser.getUser();
        Reaction fromReaction = reactionService.getReactionByIds(fromUser.getId(), toUser.getId());
        Reaction toReaction = reactionService.getReactionByIds(toUser.getId(), fromUser.getId());

        boolean mayChat = false;
        String matchType = "one";
        if(fromReaction != null && fromReaction.getType() == 3) mayChat = true;
        else if(toReaction != null && toReaction.getType() == 3) mayChat = true;
        else if(fromReaction != null && toReaction != null && fromReaction.getType() == 2 && toReaction.getType() == 2) {
            matchType = "both";
            mayChat = true;
        }

        if(!mayChat) throw new Exception("Not allowed to chat with user");

        List<Message> chats = messageService.getMessagesByIds(fromUser.getId(), toUser.getId());

        model.addAttribute("chatUser", toUser);
        model.addAttribute("matchType", matchType);
        model.addAttribute("chats", chats);
        model.addAttribute("userSelf", fromUser);

        return "chat";
    }

    @PostMapping("/chat/{id}/send")
    public String save(@RequestParam("message") String message, @PathVariable long id) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUserDetails loggedUser;
        User fromUser;
        User toUser;

        if (principal instanceof MyUserDetails) loggedUser = ((MyUserDetails) principal);
        else throw new Exception("User not logged");

        toUser = userService.getUserById(id);
        if(toUser == null)  throw new Exception("Unknown chat user");

        fromUser = loggedUser.getUser();
        Reaction fromReaction = reactionService.getReactionByIds(fromUser.getId(), toUser.getId());
        Reaction toReaction = reactionService.getReactionByIds(toUser.getId(), fromUser.getId());

        boolean mayChat = false;
        if(fromReaction != null && fromReaction.getType() == 3) mayChat = true;
        else if(toReaction != null && toReaction.getType() == 3) mayChat = true;
        else if(fromReaction != null && toReaction != null && fromReaction.getType() == 2 && toReaction.getType() == 2) mayChat = true;

        if(!mayChat) throw new Exception("Not allowed to chat with user");

        Message msg = new Message();
        msg.setMessage(message);
        msg.setSentDateTime(LocalDateTime.now());
        msg.setFromUser(fromUser);
        msg.setToUser(toUser);

        messageService.saveMessage(msg);
        return "redirect:/chat/" + id;
    }
}
