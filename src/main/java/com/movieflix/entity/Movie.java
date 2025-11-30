package com.movieflix.entity;

import java.util.Set;

import jakarta.persistence.Entity;

@Entity
public class Movie {

	private Integer movieId;

	private String title;

	private String director;

	private String studio;

	private Set<String> movieCast;

	private Integer releaseYear;

	private String poster;

}
