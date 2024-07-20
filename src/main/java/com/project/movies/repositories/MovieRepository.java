package com.project.movies.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.movies.entites.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
