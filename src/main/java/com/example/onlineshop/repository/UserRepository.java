package com.example.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onlineshop.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
