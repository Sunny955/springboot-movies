package com.project.movies.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.project.movies.dto.MovieDto;
import com.project.movies.services.movies.MovieService;

@Controller
public class MovieControllerGraphql {

    private final MovieService movieService;

    public MovieControllerGraphql(MovieService movieService) {
        this.movieService = movieService;
    }

    @QueryMapping("getMovie")
    public MovieDto getMovieHandler(@Argument Integer movieId) throws IOException {
        return movieService.getMovie(movieId);
    }

    @QueryMapping("getAllMovies")
    public List<MovieDto> getAllMoviesHandler() {
        return movieService.getAllMovies();
    }

}
