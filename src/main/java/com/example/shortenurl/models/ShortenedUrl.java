package com.example.shortenurl.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shortened_urls")
@Data
public class ShortenedUrl extends BaseModel {

    private String originalUrl;

    @Column(unique = true, nullable = false)
    private String shortUrl;

    private long expiresAt;

    @ManyToOne
    private User user;
}

