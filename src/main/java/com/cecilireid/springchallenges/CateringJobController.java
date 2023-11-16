package com.cecilireid.springchallenges;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CateringJobRepository cateringJobRepository;
    WebClient client;

    @GetMapping
    @ResponseBody
    public List<CateringJob> getCateringJobs() {
        List<CateringJob> result = new ArrayList<>();
        cateringJobRepository.findAll().forEach(result::add);
        return result;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public CateringJob getCateringJobById(@PathVariable Long id) {
        if (cateringJobRepository.existsById(id)) {
            return cateringJobRepository.findById(id).get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findByStatus")
    @ResponseBody
    public List<CateringJob> getCateringJobsByStatus(@RequestParam Status status) {
        return cateringJobRepository.findByStatus(status);
    }

    @PostMapping("/createCateringJob")
    @ResponseBody
    public CateringJob createCateringJob(@RequestBody CateringJob job) {
        return cateringJobRepository.save(job);
    }

    @PutMapping("{id}/updateCateringJob")
    @ResponseBody
    public CateringJob updateCateringJob(@RequestBody CateringJob cateringJob, @PathVariable Long id) {
        Optional<CateringJob> optionalCateringJob = cateringJobRepository.findById(id);
        if (optionalCateringJob.isPresent()) {
            cateringJob.setId(id);
            return cateringJobRepository.save(cateringJob);
        }
        throw  new HttpClientErrorException(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("{id}/patchCateringJob")
    @ResponseBody
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

    public Mono<String> getSurpriseImage() {
        return null;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleClientException() {
        return "Given order id is not found, Please check and try again.";
    }
}
