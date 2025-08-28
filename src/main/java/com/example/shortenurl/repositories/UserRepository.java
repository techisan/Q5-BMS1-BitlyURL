package com.example.shortenurl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shortenurl.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}