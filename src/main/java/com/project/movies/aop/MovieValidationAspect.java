package com.project.movies.aop;

import java.io.IOException;
import java.time.Year;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.movies.dto.MovieDto;
import com.project.movies.exceptions.EmptyFileException;
import com.project.movies.exceptions.EmptyMovieStringException;
import com.project.movies.exceptions.MovieYearException;

@Aspect
@Component
public class MovieValidationAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* com.project.movies.controllers.MovieController.addMovieHandler(..)) && args(file, movieDto)")
    public void addMoviePointcut(MultipartFile file, String movieDto) {
    }

    @Before("addMoviePointcut(file, movieDto)")
    public void validateInputs(MultipartFile file, String movieDto) throws IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty! Please send another file!");
        }

        if (movieDto.length() == 0) {
            throw new EmptyMovieStringException("Movie dto data is empty!");
        }

        MovieDto dto = objectMapper.readValue(movieDto, MovieDto.class);

        if (dto.getReleaseYear() > Year.now().getValue() || dto.getReleaseYear() < 1880) {
            throw new MovieYearException("Release year is not valid!");
        }
    }
}
