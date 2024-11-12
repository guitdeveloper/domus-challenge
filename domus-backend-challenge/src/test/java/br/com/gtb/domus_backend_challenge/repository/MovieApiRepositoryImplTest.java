package br.com.gtb.domus_backend_challenge.repository;

import br.com.gtb.domus_backend_challenge.model.Movie;
import br.com.gtb.domus_backend_challenge.response.MoviePageResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest()
class MovieApiRepositoryImplTest {

    @Mock
    private WebClient webClientMock;

    @InjectMocks
    private MovieApiRepositoryImpl movieApiRepository;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @SuppressWarnings("unchecked")
    @Test
    public void fetchMovies_ShouldReturnMoviePageResponse() throws Exception {
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(any(Function.class))).thenAnswer(invocation -> {
            Function<UriBuilder, URI> uriFunction = invocation.getArgument(0);
            UriBuilder uriBuilderMock = mock(UriBuilder.class);
            when(uriBuilderMock.queryParam(eq("page"), eq(1))).thenReturn(uriBuilderMock);
            when(uriBuilderMock.build()).thenReturn(URI.create("http://movieapi?page=1"));
            uriFunction.apply(uriBuilderMock);
            return requestHeadersSpecMock;
        });
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        MoviePageResponse moviePageResponse = new MoviePageResponse(3, List.of(new Movie("","")));
        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<MoviePageResponse>>notNull())
        ).thenReturn(Mono.just(moviePageResponse));

        Mono<MoviePageResponse> movies = movieApiRepository.fetchMovies(1);

        StepVerifier.create(movies)
                .expectNextMatches(response -> response.getData().size() == 1)
                .verifyComplete();
        Assertions.assertEquals(3, Objects.requireNonNull(movies.block()).getTotalPages());
    }
}