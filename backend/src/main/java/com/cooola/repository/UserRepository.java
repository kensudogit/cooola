package com.cooola.repository;

import com.cooola.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(User.UserRole role);

    List<User> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") User.UserRole role);
}