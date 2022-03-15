package com.crudalchemy.stanza.model;

import javax.persistence.*;
import java.util.Date;


@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    Date date;

    @Column(columnDefinition = "text")
    String body;

    //TODO: many-to-one relationship w/ user

    @ManyToOne
    ApplicationUser postingUser;

    @ManyToOne
    Topic topic;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
