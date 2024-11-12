package br.com.gtb.domus_backend_challenge.repository;

import br.com.gtb.domus_backend_challenge.model.Movie;
import br.com.gtb.domus_backend_challenge.response.MoviePageResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MovieApiRepository {
    Mono<MoviePageResponse> fetchMovies(int page);
    Mono<List<Movie>> fetchAllMovies();
}
