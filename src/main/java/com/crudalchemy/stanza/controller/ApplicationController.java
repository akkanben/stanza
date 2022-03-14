package com.crudalchemy.stanza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ApplicationController {

    @GetMapping("/")
    public RedirectView getHomePage(Model model){
        return new RedirectView();
    }

    // possibly rename in the future
    @GetMapping("/general")
    public String getGeneralBoard(Model model){
        return "board.html";
    }

    @GetMapping("/general/{topicId}")
    public String getTopicPage(@PathVariable long topicId, Model model){
        return "topic.html";
    }
}
