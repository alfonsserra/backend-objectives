package com.systelab.seed.objective.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectiveNotFoundException extends RuntimeException {

    private final String id;

    public ObjectiveNotFoundException(UUID id) {
        super("objective-not-found-" + id.toString());
        this.id = id.toString();
    }

    public String getObjectiveId() {
        return id;
    }
}