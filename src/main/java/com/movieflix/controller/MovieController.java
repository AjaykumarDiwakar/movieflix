package com.movieflix.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.DeleteExchange;

import com.movieflix.dto.MovieDto;
import com.movieflix.service.MovieService;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;

	@PostMapping("/add-movie")
	public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto)
			throws IOException {
		return ResponseEntity.status(HttpStatus.CREATED).body(movieService.addMovie(convertToMovieDto(movieDto), file));
	}

	private MovieDto convertToMovieDto(String movieDto) {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(movieDto, MovieDto.class);
	}

	@GetMapping("/{movieId}")
	public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId) {
		return ResponseEntity.ok(movieService.getMovie(movieId));
	}

	@GetMapping("/all")
	public ResponseEntity<List<MovieDto>> getAllMoviesHandler() {
		return ResponseEntity.ok(movieService.getAllMovies());
	}
	
	@PutMapping("/update/{movieId}")
	public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId,@RequestPart MultipartFile file,@RequestPart String movieDtoObj) throws IOException{
		if(file.isEmpty()) file=null;
		MovieDto movieDto=convertToMovieDto(movieDtoObj);
		return ResponseEntity.ok(movieService.updateMovie(movieId, movieDto, file));
	}
	
	@DeleteMapping("/delete/{movieId}")
	public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException{
		return ResponseEntity.ok(movieService.deleteMovie(movieId));
	}
}
