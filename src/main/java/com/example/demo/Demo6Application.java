package com.example.demo;

import com.example.demo.model.Message;
import com.example.demo.model.Reaction;
import com.example.demo.model.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.MessageService;
import com.example.demo.service.ReactionService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@SpringBootApplication
public class Demo6Application {

    private UserService userService;
    private ReactionService reactionService;
    private MessageService messageService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public Demo6Application(UserService userService, ReactionService reactionService, MessageService messageService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.reactionService = reactionService;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            User a1 = addUser("admin", "heslo", "ADMIN", "EMPTY", LocalDate.parse("2000-12-15"));
            User u1 = addUser("user1", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 1 :D",  LocalDate.parse("2011-07-12"));
            User u2 = addUser("user2", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 2 :D",  LocalDate.parse("2012-07-12"));
            User u3 = addUser("user3", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 3 :D",  LocalDate.parse("2013-07-12"));
            User u4 = addUser("user4", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 4 :D",  LocalDate.parse("2014-07-12"));

            Reaction r1 = new Reaction();
            r1.setType(2);
            r1.setFromUser(u1);
            r1.setToUser(u2);
            reactionService.saveReaction(r1);

            Reaction r2 = new Reaction();
            r2.setType(2);
            r2.setFromUser(u2);
            r2.setToUser(u1);
            reactionService.saveReaction(r2);

            Reaction r3 = new Reaction();
            r3.setType(3);
            r3.setFromUser(u1);
            r3.setToUser(u4);
            reactionService.saveReaction(r3);

            Message m1 = new Message();
            m1.setMessage("Ahoj jak se máš?");
            m1.setSentDateTime(LocalDateTime.of(2018, 5, 29, 18, 41, 16));
            m1.setFromUser(u1);
            m1.setToUser(u2);
            messageService.saveMessage(m1);

            Message m2 = new Message();
            m2.setMessage("Je to fajn, jak ty??");
            m2.setSentDateTime(LocalDateTime.of(2019, 4, 29, 22, 32, 19));
            m2.setFromUser(u2);
            m2.setToUser(u1);
            messageService.saveMessage(m2);
        };
    }

    private User addUser(String username, String password, String role, String description, LocalDate birthDate) {
        User found = userService.findByUsername(username);

        if (found == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setDescription(description);
            user.setBirthDate(birthDate);
            return userService.save(user);
        }

        return found;
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo6Application.class, args);
    }

}
