package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(){
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
