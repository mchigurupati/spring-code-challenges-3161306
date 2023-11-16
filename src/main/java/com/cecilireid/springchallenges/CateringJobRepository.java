package com.cecilireid.springchallenges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CateringJobRepository extends CrudRepository<CateringJob, Long> {

    List<CateringJob> findByStatus(Status status);
}
