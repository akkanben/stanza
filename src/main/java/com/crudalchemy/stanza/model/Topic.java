package com.crudalchemy.stanza.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String subject;
    Date mostRecentPostDate;
    @ManyToOne
    ApplicationUser originalPoster;

    //TODO: Consider if topicCreator property is required for later functionality

    // Topic timestamp will be drawn from initial post time (property of Post)
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    List<Post> topicPostList;

    public Topic() {
    }

    public Topic(String subject, ApplicationUser originalPoster) {
        this.subject = subject;
        this.originalPoster = originalPoster;
        topicPostList = new ArrayList<>();
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

    public List<Post> getTopicPostList() {
        return topicPostList;
    }

    public void setTopicPostList(List<Post> topicPostList) {
        this.topicPostList = topicPostList;
    }

    public void addNewPost(Post newPost){
        mostRecentPostDate = newPost.getDate();
        topicPostList.add(newPost);
    }

    public ApplicationUser getOriginalPoster() {
        return originalPoster;
    }

    public void setOriginalPoster(ApplicationUser originalPoster) {
        this.originalPoster = originalPoster;
    }

    public Date getMostRecentPostDate() {
        return mostRecentPostDate;
    }

    public void setMostRecentPostDate(Date mostRecentPostDate) {
        this.mostRecentPostDate = mostRecentPostDate;
    }
}
