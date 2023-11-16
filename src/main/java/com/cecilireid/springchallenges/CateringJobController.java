package com.cecilireid.springchallenges;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("cateringJobs")
public class CateringJobController {
    private static final String IMAGE_API = "https://foodish-api.herokuapp.com";

    private final CateringJobRepository cateringJobRepository;
    WebClient client;

    public CateringJobController(CateringJobRepository cateringJobRepository,  WebClient.Builder webClientBuilder) {
        this.cateringJobRepository = cateringJobRepository;
        client = webClientBuilder.baseUrl(IMAGE_API).build();
    }

    @GetMapping
    public List<CateringJob> getCateringJobs() {
        List<CateringJob> result = new ArrayList<>();
        cateringJobRepository.findAll().forEach(result::add);
        return result;
    }

    @GetMapping("/{id}")
    public CateringJob getCateringJobById(@PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            return cateringJobRepository.findById(id).get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findByStatus")
    public List<CateringJob> getCateringJobsByStatus(@RequestParam Status status) {
        return cateringJobRepository.findByStatus(status);
    }

    @PostMapping("/createCateringJob")
    public CateringJob createCateringJob(@RequestBody CateringJob job) {
        return cateringJobRepository.save(job);
    }

    @PutMapping("{id}/updateCateringJob")
    public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable Long id) {
        Optional<CateringJob> optionalCateringJob = cateringJobRepository.findById(id);
        if (optionalCateringJob.isPresent()) {
            cateringJob.setId(id);
            return cateringJobRepository.save(cateringJob);
        }
        throw  new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("{id}/patchCateringJob")
    public CateringJob patchCateringJob(@PathVariable Long id, @RequestBody JsonNode json) {
        Optional<CateringJob> optionalCateringJob = cateringJobRepository.findById(id);
        if (optionalCateringJob.isPresent()) {
            optionalCateringJob.get().setId(id);
            JsonNode menu = json.get("menu");
            if (!menu.isNull()) {
                optionalCateringJob.get().setMenu(menu.asText());
                return cateringJobRepository.save(optionalCateringJob.get());
            }
        }
        throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("getSurpriseImage")
    @ResponseBody
    public Mono<String> getSurpriseImage() {
        return client.get().uri("/api").retrieve().bodyToMono(String.class);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClientException() {
        return "Given order id is not found, Please check and try again.";
    }
}
