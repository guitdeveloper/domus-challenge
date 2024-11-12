package br.com.gtb.domus_backend_challenge.service;

import br.com.gtb.domus_backend_challenge.model.Movie;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface DirectorService {
    Mono<Map<String, List<Movie>>> getDirectors();
    Mono<Map<String, List<Movie>>> getDirectorsWithMoreMovies(int threshold);
    Mono<List<String>> getDirectorsNamesWithMoreMoviesOrdered(int threshold);
}
