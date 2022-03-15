package com.crudalchemy.stanza.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String subject;
    @ManyToOne
    ApplicationUser originalPoster;

    //TODO: Consider if topicCreator property is required for later functionality

    // Topic timestamp will be drawn from initial post time (property of Post)
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    List<Post> topicPostList;

    public Topic() {
    }

    public Topic(String subject) {
        this.subject = subject;
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
        topicPostList.add(newPost);
    }

    public ApplicationUser getOriginalPoster() {
        return originalPoster;
    }

    public void setOriginalPoster(ApplicationUser originalPoster) {
        this.originalPoster = originalPoster;
    }
}
