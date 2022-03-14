package com.crudalchemy.stanza.controller;

import com.crudalchemy.stanza.model.ApplicationUser;
import com.crudalchemy.stanza.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/")
    public RedirectView getHomePage(Model model) {
        return new RedirectView("/general");
    }

    // possibly rename in the future
    @GetMapping("/general")
    public String getGeneralBoard(Principal principal, Model model) {
        if(principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
        }
        return "board.html";
    }

    @GetMapping("/general/{topicId}")
    public String getTopicPage(@PathVariable long topicId, Model model) {
        return "topic.html";
    }

    @GetMapping("/create-account")
    public String getCreateAccountPage() {
        return "create-account.html";
    }

    //TODO: redirect to previous page from before account creation
    @PostMapping("/create-account")
    public RedirectView addNewAccount(String username, String password, String firstName, String lastName, String bio) {
        //TODO: add conditional message/logic for attempted account creation when username already exists
        if (applicationUserRepository.findByUsername(username) != null) {
            return new RedirectView("/");
        }

        String hashedPassword = passwordEncoder.encode(password);
        ApplicationUser newUser = new ApplicationUser(username, hashedPassword, firstName, lastName, bio);

        applicationUserRepository.save(newUser);
        authWithHttpServletRequest(username, password);

        return new RedirectView("/");
    }

    public void authWithHttpServletRequest(String username, String password) {
        try {
            httpServletRequest.login(username, password);
        } catch (ServletException servletException) {
            //TODO: revisit ServletException error message
            System.out.println("Error logging in");
            servletException.printStackTrace();
        }
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public RedirectView loginToApp(String username, String password){
        return new RedirectView("/");
    }
}

