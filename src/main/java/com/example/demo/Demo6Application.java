package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;

@SpringBootApplication
public class Demo6Application {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public Demo6Application(UserService userService, PasswordEncoder passwordEncoder){
        this.userService =userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner demo() {
        return (args) -> {
            addUser("admin", "heslo", "ADMIN", "EMPTY", LocalDate.parse("2000-12-15"));
            addUser("user1", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 1 :D",  LocalDate.parse("2011-07-12"));
            addUser("user2", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 2 :D",  LocalDate.parse("2012-07-12"));
            addUser("user3", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 3 :D",  LocalDate.parse("2013-07-12"));
            addUser("user4", "heslo", "USER", "Nikdy nebudu zadaný, znáte to?\n" +
                    "                Ale tak co se dá dělat no...\n" +
                    "\n" +
                    "                Prostě je to jak to je 4 :D",  LocalDate.parse("2014-07-12"));
        };
    }

    private void addUser(String username, String password, String role, String description, LocalDate birthDate) {
        if (userService.findByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setDescription(description);
            user.setBirthDate(birthDate);
            userService.save(user);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Demo6Application.class, args);
    }

}
