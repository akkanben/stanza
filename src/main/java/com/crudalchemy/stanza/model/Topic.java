package com.crudalchemy.stanza.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String subject;
    
    //TODO: Consider if topicCreator property is required for later functionality

    // Topic timestamp will be drawn from initial post time (property of Post)

    ArrayList<Post> topicPostList;

    public Topic() {
    }

    public Topic(String subject, ArrayList<Post> topicPostList) {
        this.subject = subject;
        this.topicPostList = topicPostList;
    }

    public long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<Post> getTopicPostList() {
        return topicPostList;
    }

    public void setTopicPostList(ArrayList<Post> topicPostList) {
        this.topicPostList = topicPostList;
    }
}
