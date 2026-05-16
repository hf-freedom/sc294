package com.example.knowledgebase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private String category;

    public enum UserRole {
        AUTHOR, REVIEWER, SENIOR_REVIEWER, ADMIN
    }
}