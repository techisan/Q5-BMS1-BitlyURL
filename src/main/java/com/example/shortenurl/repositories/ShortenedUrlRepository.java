package com.example.shortenurl.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shortenurl.models.ShortenedUrl;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Integer> {
    Optional<ShortenedUrl> findByShortUrl(String shortUrl);
}