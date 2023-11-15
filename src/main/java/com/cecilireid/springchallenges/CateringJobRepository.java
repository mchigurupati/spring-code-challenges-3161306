package com.cecilireid.springchallenges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface CateringJobRepository extends JpaRepository<CateringJob, Long> {

    List<CateringJob> findByStatus(Status status);
}
