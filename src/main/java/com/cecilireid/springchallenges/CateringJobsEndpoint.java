package com.cecilireid.springchallenges;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "catering-jobs")
public class CateringJobsEndpoint {
    private final CateringJobRepository cateringJobRepository;

    public CateringJobsEndpoint(CateringJobRepository cateringJobRepository) {
        this.cateringJobRepository = cateringJobRepository;
    }

    @ReadOperation
    public Map<Status, Integer> getCateringJobsMetrics() {
        Map<Status, Integer> statusIntegerMap = new HashMap<>();
        Arrays.stream(Status.values())
                .forEach(status -> statusIntegerMap.put(status, cateringJobRepository.findByStatus(status).size()));

        return statusIntegerMap;
    }


}
