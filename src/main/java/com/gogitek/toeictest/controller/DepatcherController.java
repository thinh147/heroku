package com.gogitek.toeictest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DepatcherController {
    @GetMapping
    public String index() {
        return "index.html";
    }
}
