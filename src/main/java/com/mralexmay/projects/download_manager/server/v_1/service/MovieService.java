package com.mralexmay.projects.download_manager.server.v_1.service;

import com.mralexmay.projects.download_manager.server.v_1.model.Movie;
import com.mralexmay.projects.download_manager.server.v_1.repositary.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public List getAllMovies() {
        List movies = new ArrayList();
        movieRepository.findAll().forEach(movie -> movies.add(movie));
        return movies;
    }

    public Movie getMovieById(int id) {
        return movieRepository.findById(id).get();
    }

    public void saveOrUpdate(Movie mvoie) {
        movieRepository.save(mvoie);
    }

    public void delete(int id) {
        movieRepository.deleteById(id);
    }
}
