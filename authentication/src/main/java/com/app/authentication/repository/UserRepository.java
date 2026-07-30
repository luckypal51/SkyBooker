package com.app.authentication.repository;


import com.app.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
	@Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
	Optional<User> findByEmail(@Param("email") String email);

   boolean existsByEmail(String email);

   @Query("select u from User u where u.role=:role")
   Optional<User> findAllByRole(@Param("role") String role);

   Optional<User> findByPhone(String phone);

   Optional<User> findByPassportNumber(String passportNumber);
}
