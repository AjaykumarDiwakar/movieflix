package com.movieflix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.entity.Movie;

public interface MovieRepositories extends JpaRepository<Movie, Integer> {

}
