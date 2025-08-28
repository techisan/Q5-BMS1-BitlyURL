package com.example.shortenurl.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "url_access_logs")
@Data
public class UrlAccessLog extends BaseModel {

    @ManyToOne
    private ShortenedUrl shortenedUrl;

    private long accessedAt;
}

