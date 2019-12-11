package com.systelab.seed.objective.repository;

import com.systelab.seed.objective.model.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObjectiveRepository extends JpaRepository<Objective, UUID>, RevisionRepository<Objective, UUID, Integer> {

    Optional<Objective> findById(@Param("id") UUID id);
}