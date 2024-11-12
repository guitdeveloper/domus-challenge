package br.com.gtb.domus_backend_challenge.service;

import br.com.gtb.domus_backend_challenge.model.Movie;
import br.com.gtb.domus_backend_challenge.repository.MovieApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private final MovieApiRepository movieApiRepository;

    public DirectorServiceImpl(MovieApiRepository movieApiRepository) {
        this.movieApiRepository = movieApiRepository;
    }

    public Mono<Map<String, List<Movie>>> getDirectors() {
        return movieApiRepository.fetchAllMovies()
                .map(movies -> movies.stream()
                        .collect(Collectors.groupingBy(Movie::getDirector)));
    }

    public Mono<Map<String, List<Movie>>> getDirectorsWithMoreMovies(int threshold) {
        return getDirectors()
                .map(directorMap -> directorMap.entrySet().stream()
                        .filter(entry -> entry.getValue().size() > threshold)
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, _) -> oldValue,
                                LinkedHashMap::new
                        ))
                );
    }

    public Mono<List<String>> getDirectorsNamesWithMoreMoviesOrdered(int threshold) {
        return getDirectorsWithMoreMovies(threshold)
                .map(directorMap -> directorMap.keySet().stream()
                    .sorted()
                    .collect(Collectors.toList())
                );
    }
}
