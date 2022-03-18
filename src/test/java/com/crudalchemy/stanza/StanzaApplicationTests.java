package com.crudalchemy.stanza;
import com.crudalchemy.stanza.model.ApplicationUser;
import com.crudalchemy.stanza.model.Post;
import com.crudalchemy.stanza.model.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StanzaApplicationTests {

    @Autowired
    MockMvc mockMvc;


    ///// ROUTES //////
    @Test
    @DisplayName("board")
    void testGeneralThreadPage() throws Exception {
        mockMvc.perform(get("/general"))
                .andExpect(content().string(containsString("General Thread Page")))
                .andExpect(content().string(containsString("New Topic")))
                .andExpect(content().string(containsString("Topic Title")))
                .andExpect(content().string(containsString("Author")))
                .andExpect(content().string(containsString("Last Post Date")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("create-account")
    void testCreateAccountPage() throws Exception {
        mockMvc.perform(get("/create-account"))
                .andExpect(content().string(containsString("Create An Account")))
                .andExpect(content().string(containsString("Enter New Account Details")))
                .andExpect(content().string(containsString("Username")))
                .andExpect(content().string(containsString("Password")))
                .andExpect(content().string(containsString("First Name")))
                .andExpect(content().string(containsString("Last Name")))
                .andExpect(content().string(containsString("User Bio")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("login")
    void testLogInPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(content().string(containsString("Log In")))
                .andExpect(content().string(containsString("Username")))
                .andExpect(content().string(containsString("Password")))
                .andExpect(status().isOk());
    }


    ///// ROUTES REDIRECTS - REQUIRES USER LOGIN //////
    @Test
    @DisplayName("required login to view profile- redirects")
    void testMyProfile() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(content().string(containsString("")))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("required login to create topic- redirects")
    void testCreateTopicRedirects() throws Exception {
        mockMvc.perform(get("/create-topic"))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @DisplayName("required login to add post- redirects")
    void testAddPostRedirects() throws Exception {
        mockMvc.perform(get("/add-post"))
                .andExpect(redirectedUrl("http://localhost/login"));
    }


    ///// INSTANTIATE NEW OBJECTS /////
    @Test
    @DisplayName("instantiate a new user")
    void testInstantiateNewUser(){
        ApplicationUser newUser = new ApplicationUser();
        Assert.isInstanceOf(ApplicationUser.class, newUser);
    }


    @Test
    @DisplayName("instantiate a new topic")
    void testCreateTopic(){
        Topic topic = new Topic();
        Assert.isInstanceOf(Topic.class, topic);
    }

    @Test
    @DisplayName("create new account")
    void testCreateNewAccount(){
        ApplicationUser newAccount = new ApplicationUser("username", "password", "firstName", "lastName", "my Bio");
        assert(newAccount.getUsername().equals("username"));
        assert(newAccount.getFirstName().equals("firstName"));
        assert(newAccount.getLastName().equals("lastName"));
        assert(newAccount.getBio().equals("my Bio"));
    }

    @Test
    @DisplayName("create a new post")
    void testCreatePost(){
        ApplicationUser newUser = new ApplicationUser("myUserName", "p@ssword", "tom", "smith", "my bio");
        Date date = new Date();
        Topic topic = new Topic("Title of Subject", newUser);
        Post post = new Post("I'm a new post", newUser, topic, date);
        topic.addNewPost(post);
        assert(newUser.getUsername().equals("myUserName"));
        assert(newUser.getBio().equals("my bio"));
        assert(post.getDate().equals(date));
        assert(post.getPostingUser().equals(newUser));
        assert(post.getBody().equals("I'm a new post"));
    }


    @Test
    @DisplayName("create a new post 2")
    void testAddNewTopic(){
        ApplicationUser newUser = new ApplicationUser("username", "password", "firstName", "lastName", "my Bio");
        Date date = new Date();
        Topic topic = new Topic("Title of Subject", newUser);
        Post newPost = new Post("body of new Post", newUser, topic, date);
        ArrayList<Topic> userThreadList = new ArrayList<>();
        userThreadList.add(topic);
        ArrayList<Post> topicPostList = new ArrayList<>();
        topicPostList.add(newPost);
        newUser.addPostToUserPostList(newPost);
        newUser.addTopicToUserTopicList(topic);
        assert(newUser.getUserThreadList().equals(userThreadList));
        assert(newUser.getUserPostList().equals(topicPostList));
    }

}
