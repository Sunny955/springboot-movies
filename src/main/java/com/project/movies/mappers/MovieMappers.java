package com.project.movies.mappers;

import org.springframework.stereotype.Component;

import com.project.movies.dto.MovieDto;
import com.project.movies.entites.Movie;

@Component
public class MovieMappers {
    public MovieDto movieToMovieDto(Movie movie, String posterUrl) {
        return MovieDto.builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .director(movie.getDirector())
                .movieCast(movie.getMovieCast())
                .poster(movie.getPoster())
                .releaseYear(movie.getReleaseYear())
                .studio(movie.getStudio())
                .posterUrl(posterUrl)
                .build();
    }

    public Movie movieDtotoMovie(MovieDto movieDto) {
        return Movie.builder()
                .title(movieDto.getTitle())
                .director(movieDto.getDirector())
                .studio(movieDto.getStudio())
                .movieCast(movieDto.getMovieCast())
                .releaseYear(movieDto.getReleaseYear())
                .poster(movieDto.getPoster())
                .build();
    }
}
