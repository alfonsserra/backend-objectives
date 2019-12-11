package com.systelab.seed.objective.service;

import com.systelab.seed.objective.model.Objective;
import com.systelab.seed.objective.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ObjectiveService {

    private final ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveService(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    public Page<Objective> getAllObjectives(Pageable pageable) {
        final PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "name");
        return this.objectiveRepository.findAll(page);
    }

    public Objective getObjective(UUID objectiveId) {
        return this.objectiveRepository.findById(objectiveId).orElseThrow(() -> new ObjectiveNotFoundException(objectiveId));
    }

    public Objective createObjective(Objective objective) {
        return this.objectiveRepository.save(objective);
    }

    public Objective updateObjective(UUID id, Objective objective) {
        return this.objectiveRepository.findById(id)
                .map(existing -> {
                    objective.setId(id);
                    return this.objectiveRepository.save(objective);
                }).orElseThrow(() -> new ObjectiveNotFoundException(id));
    }

    public Objective removeObjective(UUID id) {
        return this.objectiveRepository.findById(id)
                .map(existing -> {
                    objectiveRepository.delete(existing);
                    return existing;
                }).orElseThrow(() -> new ObjectiveNotFoundException(id));
    }

    public Double evaluateObjectives() {
        return this.objectiveRepository
                .findAll()
                .stream()
                .mapToDouble(Objective::getEvaluation)
                .average()
                .orElse(0);
    }
}
