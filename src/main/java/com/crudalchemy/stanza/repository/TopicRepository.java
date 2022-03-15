package com.crudalchemy.stanza.repository;

import com.crudalchemy.stanza.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
