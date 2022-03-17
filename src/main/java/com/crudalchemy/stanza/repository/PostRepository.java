package com.crudalchemy.stanza.repository;

import com.crudalchemy.stanza.model.ApplicationUser;
import com.crudalchemy.stanza.model.Post;
import com.crudalchemy.stanza.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPostingUser(ApplicationUser postingUser);
}
