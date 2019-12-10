package com.mcy.crawlerdouban.repository;

import com.mcy.crawlerdouban.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
