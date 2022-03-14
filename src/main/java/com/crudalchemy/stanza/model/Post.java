package com.crudalchemy.stanza.model;

import javax.persistence.*;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(columnDefinition = "text")
    String body;

    //TODO: many-to-one relationship w/ user


    ApplicationUser postingUser;

    public Post() {
    }

    public Post(String body, ApplicationUser postingUser) {
        this.body = body;
        this.postingUser = postingUser;
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ApplicationUser getPostingUser() {
        return postingUser;
    }

    public void setPostingUser(ApplicationUser postingUser) {
        this.postingUser = postingUser;
    }

}
