package com.systelab.seed.objective.controller;

import com.systelab.seed.objective.model.Objective;
import com.systelab.seed.objective.model.ObjectiveSummary;
import com.systelab.seed.objective.service.ObjectiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@Api(value = "Objective", description = "API for objective management", tags = {"Objective"})
@RestController()
// Bad idea to have that on production
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "Authorization", allowCredentials = "true")
@RequestMapping(value = "/csw/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ObjectiveController {

    private final ObjectiveService objectiveService;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    @ApiOperation(value = "Get all Objectives", authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("objectives")
    public ResponseEntity<Page<Objective>> getAllObjectives(Pageable pageable) {
        return ResponseEntity.ok(this.objectiveService.getAllObjectives(pageable));
    }

    @ApiOperation(value = "Get an Objective", authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("objectives/{uid}")
    public ResponseEntity<Objective> getObjective(@PathVariable("uid") UUID id) {
        return ResponseEntity.ok(this.objectiveService.getObjective(id));
    }

    @ApiOperation(value = "Create an Objective", authorizations = {@Authorization(value = "Bearer")})
    @PostMapping("objectives/objective")
    public ResponseEntity<Objective> createObjective(@RequestBody @ApiParam(value = "Objective", required = true) @Valid Objective objective) {
        Objective createdObjective = this.objectiveService.createObjective(objective);
        URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/objectives/{id}").buildAndExpand(createdObjective.getId()).toUri();
        return ResponseEntity.created(uri).body(createdObjective);
    }

    @ApiOperation(value = "Create or Update (idempotent) an existing Objective", authorizations = {@Authorization(value = "Bearer")})
    @PutMapping("objectives/{uid}")
    public ResponseEntity<Objective> updateObjective(@PathVariable("uid") UUID id, @RequestBody @ApiParam(value = "Objective", required = true) @Valid Objective objective) {
        Objective updatedObjective = this.objectiveService.updateObjective(id, objective);
        URI selfLink = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
        return ResponseEntity.created(selfLink).body(updatedObjective);
    }

    @ApiOperation(value = "Delete an Objective", authorizations = {@Authorization(value = "Bearer")})
    @DeleteMapping("objectives/{uid}")
    public ResponseEntity removeObjective(@PathVariable("uid") UUID id) {
        this.objectiveService.removeObjective(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get the Year Summary", authorizations = {@Authorization(value = "Bearer")})
    @GetMapping("objectives/evaluation")
    public ResponseEntity<ObjectiveSummary> getSummary() {
        return ResponseEntity.ok(new ObjectiveSummary(this.objectiveService.evaluateObjectives()));
    }
}