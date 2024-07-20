package com.project.movies.dto;

import com.project.movies.auth.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    private Integer userId;

    private String name;

    private String username;

    private String email;

    private UserRole role;

    private String message;
}
