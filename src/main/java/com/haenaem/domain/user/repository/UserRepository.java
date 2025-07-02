package com.haenaem.domain.user.repository;

import com.haenaem.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean exitsByEmail(String email);

  Optional<User> findByEmail(String email);
}
