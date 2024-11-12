package br.com.gtb.domus_backend_challenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app.movie.url}")
    private String movieUrl;

    @Bean
    public WebClient movieWebClient(WebClient.Builder builder) {
        return builder.baseUrl(movieUrl).build();
    }
}
