package com.crudalchemy.stanza.repository;

import com.crudalchemy.stanza.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
   public ApplicationUser findByUsername(String username);
}
