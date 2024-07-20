package com.project.movies.mappers;

import org.springframework.stereotype.Component;

import com.project.movies.auth.entities.User;
import com.project.movies.dto.UserDto;

@Component
public class UserMappers {
    public UserDto repoToDto(User u) {
        return UserDto.builder()
                .userId(u.getUserId())
                .email(u.getEmail())
                .name(u.getName())
                .username(u.getUsername())
                .role(u.getRole())
                .build();
    }

    public User dtoToRepo(UserDto u) {
        return User.builder()
                .email(u.getEmail())
                .name(u.getName())
                .username(u.getUsername())
                .role(u.getRole())
                .build();
    }
}
