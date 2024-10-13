package com.alishahidi.api.core.entity;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T extends BaseEntity, C extends BaseDto, U extends BaseDto, L extends BaseDto> {

    protected abstract BaseService<T, C, U, L> getService();

    @PostMapping
    public ResponseEntity<L> create(@RequestBody C createDto) {
        L createdEntity = getService().create(createDto);
        return ResponseEntity.ok(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<L> update(@PathVariable Long id, @RequestBody U updateDto) {
        L updatedEntity = getService().update(id, updateDto);
        return ResponseEntity.ok(updatedEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<L> findById(@PathVariable Long id) {
        L entity = getService().findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<L>> findAll() {
        List<L> entities = getService().findAll();
        return ResponseEntity.ok(entities);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        getService().deleteLogical(id);
        return ResponseEntity.noContent().build();
    }
}
