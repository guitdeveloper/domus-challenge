package br.com.gtb.domus_backend_challenge.controller;

import br.com.gtb.domus_backend_challenge.exception.ResourceNotFoundException;
import br.com.gtb.domus_backend_challenge.service.DirectorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping({"/api"})
public class DirectorController {

    @Autowired
    private final DirectorServiceImpl directorService;

    public DirectorController(DirectorServiceImpl directorService) {
        this.directorService = directorService;
    }

    @GetMapping({"/directors"})
    public Mono<List<String>> getDirectors(@RequestParam int threshold) {
        return directorService.getDirectorsNamesWithMoreMoviesOrdered(threshold)
                .flatMap(directorList -> {
                    if (directorList.isEmpty()) {
                        return Mono.error(new ResourceNotFoundException("No director found with more than " + threshold + " movies"));
                    }
                    return Mono.just(directorList);
                });
    }
}
