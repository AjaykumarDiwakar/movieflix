package com.movieflix.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movieflix.dto.MovieDto;
import com.movieflix.entity.Movie;
import com.movieflix.repositories.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private FileService fileService;

	@Value("${project.poster}")
	private String path;

	@Value("${base.url}")
	private String baseUrl;

	@Override
	public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
		// 1.upload the file
		String uploadedFileName = fileService.uploadFile(path, file);

		// 2.set the value of field 'Poster' as filename
		movieDto.setPoster(uploadedFileName);

		// 3.map dto to movie Object
		Movie movie = Movie.builder().movieId(movieDto.getMovieId()).title(movieDto.getTitle())
				.director(movieDto.getDirector()).studio(movieDto.getStudio()).movieCast(movieDto.getMovieCast())
				.releaseYear(movieDto.getReleaseYear()).poster(movieDto.getPoster()).build();

		// 4. save the movie object-->Saved Movie Object
		Movie savedMovie = movieRepository.save(movie);

		// 5. generate the posterUrl:The Url by hitting which we will get the file
		String posterUrl = baseUrl + "/file/" + uploadedFileName;

		// 6. map Movie object to DTO object and return it
		MovieDto response = MovieDto.builder().movieId(savedMovie.getMovieId()).title(savedMovie.getTitle())
				.director(savedMovie.getDirector()).studio(savedMovie.getStudio()).movieCast(savedMovie.getMovieCast())
				.releaseYear(savedMovie.getReleaseYear()).poster(savedMovie.getPoster()).posterUrl(posterUrl).build();
		return response;
	}

	@Override
	public MovieDto getMovie(Integer movieId) {
		// 1.check the data in db if exists then fetch the data of given id
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Found!"));

		// 2.generate posterurl
		String posterUrl = baseUrl + "/file/" + movie.getPoster();

		// 3 map to MovieDto object and returns it
		return MovieDto.builder().movieId(movie.getMovieId()).title(movie.getTitle()).director(movie.getDirector())
				.studio(movie.getStudio()).movieCast(movie.getMovieCast()).releaseYear(movie.getReleaseYear())
				.poster(movie.getPoster()).posterUrl(posterUrl).build();

	}

	@Override
	public List<MovieDto> getAllMovies() {
		// 1.fetch all data from DB
		List<Movie> movies = movieRepository.findAll();

		// 2.Iterate through the list ,generate posterUrl for each movie obj,and map to
		// MovieDto Obj
		List<MovieDto> response = new ArrayList<MovieDto>();

		for (Movie movie : movies) {
			String posterUrl = baseUrl + "/file/" + movie.getPoster();
			response.add(MovieDto.builder().movieId(movie.getMovieId()).title(movie.getTitle())
					.director(movie.getDirector()).studio(movie.getStudio()).movieCast(movie.getMovieCast())
					.releaseYear(movie.getReleaseYear()).poster(movie.getPoster()).posterUrl(posterUrl).build());
		}
		return response;
	}

}
