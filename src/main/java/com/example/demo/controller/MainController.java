package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.GroupMembershipService;
import com.example.demo.service.GroupService;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class MainController {

    private UserService userService;
    private ReactionService reactionService;
    private GroupService groupService;
    private GroupMembershipService groupMembershipService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MainController(UserService userService, GroupService groupService, GroupMembershipService groupMembershipService, ReactionService reactionService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.groupService = groupService;
        this.groupMembershipService = groupMembershipService;
        this.reactionService = reactionService;
        this.passwordEncoder = passwordEncoder;
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

        List<Group> groups = groupService.getAllGroups();
        List<Group> memberGroups = groupMembershipService.getGroupsForUser(fromUser.getId());
        List<Group> notMemberGroups = new ArrayList<>();

        for(Group group : groups) {
            boolean isMember = false;

            // check if is member already
            for(Group g : memberGroups) {
                if(g.getId() == group.getId()) isMember = true;
            }

            if(!isMember) notMemberGroups.add(group);
        }

        model.addAttribute("swipeUser", swipeUser);
        model.addAttribute("memberGroupList", memberGroups);
        model.addAttribute("notMemberGroupList", notMemberGroups);
        return "main";
    }

    @GetMapping("/group/toggle/{id}")
    public String toggle(Model model, @PathVariable long id) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MyUserDetails loggedUser;
        User fromUser;

        if (principal instanceof MyUserDetails) loggedUser = ((MyUserDetails) principal);
        else throw new Exception("User not logged");

        fromUser = loggedUser.getUser();

        Optional<Group> groupOpt = groupService.getGroupById(id);

        if (groupOpt.isEmpty()) {
            throw new Exception("Group not found");
        }

        Optional<GroupMembership> membershipOpt = groupMembershipService.getMembershipByUserAndGroup(fromUser.getId(), id);

        if (membershipOpt.isPresent()) {
            groupMembershipService.deleteGroupMembershipById(membershipOpt.get().getId());
        } else {
            GroupMembership newMembership = new GroupMembership();
            newMembership.setUser(fromUser);
            newMembership.setGroup(groupOpt.get());
            groupMembershipService.saveGroupMembership(newMembership);
        }

        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signup(Model model) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MyUserDetails) return "redirect:/";

        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup/create")
    public String save(@Valid User user, BindingResult bindingResult) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MyUserDetails) return "redirect:/";

        if(bindingResult.hasErrors()){
            return "redirect:/signup";
        }

        User found = userService.findByUsername(user.getUsername());
        if (found == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
        } else {
            return "redirect:/signup?error";
        }

        userService.save(user);
        return "redirect:/login";
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
