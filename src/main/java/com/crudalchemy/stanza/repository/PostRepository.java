package com.crudalchemy.stanza.repository;

import com.crudalchemy.stanza.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
