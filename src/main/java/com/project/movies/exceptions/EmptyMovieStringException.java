package com.project.movies.exceptions;

public class EmptyMovieStringException extends RuntimeException {
    public EmptyMovieStringException(String message) {
        super(message);
    }
}
