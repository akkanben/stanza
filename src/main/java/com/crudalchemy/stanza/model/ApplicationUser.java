package com.crudalchemy.stanza.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String username;
    String password;
    String firstName;
    String lastName;

    @Column(columnDefinition = "text")
    String bio;

    @OneToMany(mappedBy = "postingUser", cascade = CascadeType.ALL)
    List<Post> userPostList;

    @OneToMany(mappedBy = "originalPoster", cascade = CascadeType.ALL)
    List<Topic> userThreadList;


    public ApplicationUser() {
    }

    //TODO: revisit `bio` in constructor/acct. setup
    public ApplicationUser(String username, String password, String firstName, String lastName, String bio) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        userPostList = new ArrayList<>();
        userThreadList = new ArrayList<>();
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Post> getUserPostList() {
        return userPostList;
    }

    public void setUserPostList(List<Post> userPostList) {
        this.userPostList = userPostList;
    }

    public List<Topic> getUserThreadList() {
        return userThreadList;
    }

    public void setUserThreadList(List<Topic> userThreadList) {
        this.userThreadList = userThreadList;
    }

    public void addPostToUserPostList(Post post) {
        userPostList.add(post);
    }

    public void addTopicToUserTopicList(Topic topic) {
        userThreadList.add(topic);
    }
}
