package com.crudalchemy.stanza.controller;

import com.crudalchemy.stanza.model.ApplicationUser;
import com.crudalchemy.stanza.model.Post;
import com.crudalchemy.stanza.model.Topic;
import com.crudalchemy.stanza.repository.ApplicationUserRepository;
import com.crudalchemy.stanza.repository.PostRepository;
import com.crudalchemy.stanza.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.reverseOrder;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/")
    public RedirectView getHomePage(Model model) {
        return new RedirectView("/general");
    }

    // possibly rename in the future
    @GetMapping("/general")
    public String getGeneralBoard(Principal principal, Model model) {
        if (principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        List<Topic> topicList = topicRepository.findByOrderByMostRecentPostDateDesc();
        if (topicList != null)
            model.addAttribute("topicList", topicList);
        return "board.html";
    }

    @GetMapping("/general/{topicId}")
    public String getTopicPage(Principal principal, @PathVariable long topicId, Model model) {
        if (principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        Topic currentTopic = topicRepository.getById(topicId);
        model.addAttribute("currentTopic", currentTopic);
        return "topic.html";
    }

    // Alternate GetMapping for linking to a specific post (from profile)
    @GetMapping("/general/{topicId}/{postId}")
    public RedirectView getTopicPage(Principal principal, @PathVariable long topicId, @PathVariable String postId, Model model) {
        if(principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        Topic currentTopic = topicRepository.getById(topicId);
        model.addAttribute("currentTopic", currentTopic);
        return new RedirectView("/general/" + topicId + "#" + postId);
    }

    @GetMapping("/create-account")
    public String getCreateAccountPage(Principal principal, Model model) {
        if (principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "create-account.html";
    }

    //TODO: redirect to previous page from before account creation
    @PostMapping("/create-account")
    public RedirectView addNewAccount(RedirectAttributes errors, String username, String password, String firstName, String lastName, String bio) {

        ArrayList<String> errorList = new ArrayList<>();
        if (username.length() < 3
                || username.length() > 25) {
            errorList.add("Usernames must be between 3 and 20 characters long!");
        }
        if (password.length() < 8) {
            errorList.add("Passwords must be at least 8 characters long!");
        }
        if (applicationUserRepository.findByUsername(username) != null) {
            errorList.add("Choose another username");
        }
        if (errorList.size() > 0) {
            errors.addFlashAttribute("errorMessageList", errorList);
            return new RedirectView("/create-account");
        }

        String hashedPassword = passwordEncoder.encode(password);
        ApplicationUser newUser = new ApplicationUser(username, hashedPassword, firstName, lastName, bio);
        Random random = new Random();
        newUser.setAvatar(random.nextInt(1,51) + ".png");
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
    public String getLoginPage(Principal principal, Model model) {
        if (principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "login.html";
    }

    @PostMapping("/login")
    public RedirectView loginToApp(RedirectAttributes errors, String username, String password) {
        ArrayList<String> errorList = new ArrayList<>();
        if(!applicationUserRepository.findByUsername(username).getPassword().equals(password)
            || applicationUserRepository.findByUsername(username) == null){
            errorList.add("Invalid credentials");
        }

        if (errorList.size() > 0) {
            errors.addFlashAttribute("errorMessageList", errorList);
            return new RedirectView("/login");
        }


        return new RedirectView("/");
    }

    @PostMapping("/logout")
    public RedirectView logoutUser(Principal principal) {
        if (principal != null) {
            try {
                httpServletRequest.logout();
            } catch (ServletException servletException) {
                System.out.println("Error logging out");
                servletException.printStackTrace();
            }
        }
        return new RedirectView("/");
    }

    @PostMapping("/create-topic")
    public RedirectView addNewTopic(Principal principal, Model model, String subject, String body) {
        if (principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
            Topic newTopic = new Topic(subject, loggedInUser);
            Date date = new Date();
            Post newPost = new Post(body, loggedInUser, newTopic, date);
            loggedInUser.addPostToUserPostList(newPost);
            newTopic.addNewPost(newPost);
            loggedInUser.addTopicToUserTopicList(newTopic);
            topicRepository.save(newTopic);
        }
        return new RedirectView("/");
    }

    @GetMapping("/create-topic")
    public String getNewTopicPage(Principal principal, Model model) {
        if (principal != null) {
            // revisit - possibly change loggedInUser
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "new-topic.html";
    }

    @PostMapping("/add-post")
    public RedirectView addNewPost(Principal principal, Model model, String body, long topicId) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
            Topic topic = topicRepository.getById(topicId);
            Post addedPost = new Post(body, loggedInUser, topic, new Date());
            topic.addNewPost(addedPost);
            topicRepository.save(topic);

        }
        return new RedirectView("general/" + topicId + "#goto-bottom");
    }

    @GetMapping("/profile/{userID}")
    public String getUserProfilePage(@PathVariable long userID, Principal principal, Model model) {
        ApplicationUser currentProfileUser = applicationUserRepository.getById(userID);
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        try {
            currentProfileUser.getFirstName();
        } catch (EntityNotFoundException entityNotFoundException) {
            model.addAttribute("errorMessage", "Could not find a user for that id!");
            return "profile.html";
        }
        model.addAttribute("currentProfileUser", currentProfileUser);
        List<Post> lastFivePostsList = postRepository.findAllByPostingUser(currentProfileUser).stream().sorted((a, b) -> {
            return b.getDate().compareTo(a.getDate());
        }).toList();
        model.addAttribute("lastFivePostsList", lastFivePostsList);
        int postCount = 0;
        model.addAttribute(postCount);
        return "profile.html";
    }


    @GetMapping("/profile")
    public String getUserProfilePage(Principal principal, Model model) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return "my-profile.html";
    }

    @PostMapping("/update-account")
    public RedirectView editUserAccount(Principal principal, Model model, String firstName, String lastName, String bio) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            if (firstName != "") loggedInUser.setFirstName(firstName);
            if (lastName != "") loggedInUser.setLastName(lastName);
            if (bio != "") loggedInUser.setBio(bio);
            applicationUserRepository.save(loggedInUser);
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return new RedirectView("/profile");
    }

    @PostMapping("/re-roll-avatar")
    public RedirectView reRollAvatar(Principal principal, Model model) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            Random random = new Random();
            loggedInUser.setAvatar(random.nextInt(1,51) + ".png");
            applicationUserRepository.save(loggedInUser);
            model.addAttribute("loggedInUser", loggedInUser);
        }
        return new RedirectView("/profile");
    }


    @GetMapping("/about")
    public String getAboutUsPage(Principal principal, Model model) {
        if(principal != null){
        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
        model.addAttribute("loggedInUser", loggedInUser);
        }
        return "about-us.html";
    }

    @PostMapping("/delete-post")
    public RedirectView deletePost(Principal principal, Model model, long postId, long topicId) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
            if (loggedInUser.getAdmin()) {
                Topic toDeleteTopic = topicRepository.getById(topicId);
                List<Post> toDeleteTopicPostList = toDeleteTopic.getTopicPostList();
                if (toDeleteTopicPostList.get(0).getId() == postId) {
                    topicRepository.deleteById(topicId);
                    return new RedirectView("/general");
                } else {
                    toDeleteTopicPostList = toDeleteTopicPostList.stream().filter(element -> element.getId() != postId).collect(Collectors.toList());
                    toDeleteTopic.setTopicPostList(toDeleteTopicPostList);
                    topicRepository.save(toDeleteTopic);
                    postRepository.deleteById(postId);
                }
            }
        }
        return new RedirectView("/general/" + topicId + "/" + (postId - 1));
    }

    @PostMapping("/delete-topic")
    public RedirectView deleteTopic(Principal principal, Model model, long topicId) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
            if (loggedInUser.getAdmin())
                topicRepository.deleteById(topicId);
        }
        return new RedirectView("/general");
    }

    @PostMapping("/toggle-admin")
    public RedirectView enableAdmin(Principal principal, Model model, long userId) {
        if (principal != null) {
            ApplicationUser loggedInUser = applicationUserRepository.findByUsername(principal.getName());
            model.addAttribute("loggedInUser", loggedInUser);
            ApplicationUser newAdmin = applicationUserRepository.getById(userId);
            if (loggedInUser.getAdmin() && !newAdmin.getAdmin() ) {
                newAdmin.setAdmin(true);
                applicationUserRepository.save(newAdmin);
            }
            else if (loggedInUser.getAdmin() && newAdmin.getAdmin()) {
                newAdmin.setAdmin(false);
                applicationUserRepository.save(newAdmin);
            }
        }
        return new RedirectView("/profile/" + userId);
    }

}

