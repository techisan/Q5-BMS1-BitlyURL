package com.example.shortenurl.models;

import lombok.Data;

import jakarta.persistence.*;


@MappedSuperclass
@Data
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}

