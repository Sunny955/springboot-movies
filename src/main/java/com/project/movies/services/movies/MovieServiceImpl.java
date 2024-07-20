package com.project.movies.services.movies;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.movies.dto.MovieDto;
import com.project.movies.dto.MoviePageResponse;
import com.project.movies.entites.Movie;
import com.project.movies.exceptions.FileExistsException;
import com.project.movies.exceptions.MovieNotFoundException;
import com.project.movies.mappers.MovieMappers;
import com.project.movies.repositories.MovieRepository;
import com.project.movies.services.file.FileService;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final FileService fileService;

    private final MovieMappers mappers;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService, MovieMappers movieMappers) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
        this.mappers = movieMappers;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileExistsException("File already exists, Please enter another file name!");
        }

        String uploadFile = fileService.uploadFile(path, file);

        movieDto.setPoster(uploadFile);

        Movie movie = mappers.movieDtotoMovie(movieDto);

        Movie saveMovie = movieRepository.save(movie);

        String posterUrl = baseUrl + "/file/" + uploadFile;

        MovieDto response = mappers.movieToMovieDto(saveMovie, posterUrl);

        return response;
    }

    @Override
    public MovieDto getMovie(Integer movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        String posterUrl = baseUrl + "/file/" + movie.getStudio();

        MovieDto response = mappers.movieToMovieDto(movie, posterUrl);

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie m : movies) {
            String posterUrl = baseUrl + "/file/" + m.getPoster();

            MovieDto mdto = mappers.movieToMovieDto(m, posterUrl);

            movieDtos.add(mdto);
        }

        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        // 2. if file is null, do nothing
        // if file is not null, then delete existing file associated with the record,
        // and upload the new file

        String fileName = mv.getPoster();
        if (file != null) {
            Files.deleteIfExists(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }

        movieDto.setPoster(fileName);

        Movie updatedMovie = movieRepository.save(mappers.movieDtotoMovie(movieDto));

        String posterUrl = baseUrl + "/file/" + fileName;

        MovieDto response = mappers.movieToMovieDto(updatedMovie, posterUrl);

        return response;
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException {
        Movie mv = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));
        Integer id = mv.getMovieId();

        Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));

        movieRepository.delete(mv);

        return "Movie deleted with id = " + id;
    }

    @Override
    public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie m : movies) {
            String posterUrl = baseUrl + "/file/" + m.getPoster();

            MovieDto mdto = mappers.movieToMovieDto(m, posterUrl);

            movieDtos.add(mdto);
        }

        return new MoviePageResponse(movieDtos, pageNumber, pageSize, moviePages.getTotalElements(),
                moviePages.getTotalPages(), moviePages.isLast());
    }

    @Override
    public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy,
            String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();

        for (Movie m : movies) {
            String posterUrl = baseUrl + "/file/" + m.getPoster();

            MovieDto mdto = mappers.movieToMovieDto(m, posterUrl);

            movieDtos.add(mdto);
        }

        return new MoviePageResponse(movieDtos, pageNumber, pageSize, moviePages.getTotalElements(),
                moviePages.getTotalPages(), moviePages.isLast());
    }

}
