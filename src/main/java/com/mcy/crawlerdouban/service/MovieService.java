package com.mcy.crawlerdouban.service;

import com.mcy.crawlerdouban.entity.Movie;
import com.mcy.crawlerdouban.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public void save(Movie movie) {
        movieRepository.save(movie);
    }
}
