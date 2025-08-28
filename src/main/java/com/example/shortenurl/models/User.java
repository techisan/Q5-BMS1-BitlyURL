package com.example.shortenurl.models;

import lombok.Data;

import jakarta.persistence.*;


@Entity
@Table(name = "users")   // avoid reserved keyword
@Data
public class User extends BaseModel {

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private UserPlan userPlan;
}

