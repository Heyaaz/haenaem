package com.haenaem.domain.user.repository;

import com.haenaem.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u ORDER BY u.createdAt DESC, u.id DESC")
  Page<User> findAllWithPaging(Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.nickname LIKE %:keyword% OR u.email LIKE %:keyword% ORDER BY u.createdAt DESC, u.id DESC")
  Page<User> findByKeywordWithPaging(@Param("keyword") String keyword, Pageable pageable);
}
