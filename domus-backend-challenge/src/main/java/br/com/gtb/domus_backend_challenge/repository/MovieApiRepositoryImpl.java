package br.com.gtb.domus_backend_challenge.repository;

import br.com.gtb.domus_backend_challenge.model.Movie;
import br.com.gtb.domus_backend_challenge.response.MoviePageResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class MovieApiRepositoryImpl implements MovieApiRepository {

    private final String QUERY_PARAM_PAGE = "page";
    private final int PAGE_ONE = 1;

    private final WebClient movieWebClient;

    public MovieApiRepositoryImpl(WebClient movieWebClient) {
        this.movieWebClient = movieWebClient;
    }

    @Override
    public Mono<MoviePageResponse> fetchMovies(int page) {
        return movieWebClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam(QUERY_PARAM_PAGE, page).build())
                .retrieve()
                .bodyToMono(MoviePageResponse.class);
    }

    @Override
    public Mono<List<Movie>> fetchAllMovies() {
        return fetchMovies(PAGE_ONE)
                .flatMapMany(firstResponse -> {
                    int totalPages = firstResponse.getTotalPages();
                    return Flux.range(PAGE_ONE, totalPages)
                            .flatMap(this::fetchMovies)
                            .flatMap(movie -> Flux.fromIterable(movie.getData()));
                })
                .collectList();
    }
}
