package com.alishahidi.api.core.entity;

import com.alishahidi.api.core.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T extends BaseEntity, C extends BaseDto, U extends BaseDto, L extends BaseDto> {

    protected abstract BaseService<T, C, U, L> getService();

    @PostMapping
    public ResponseEntity<ApiResponse<L>> create(@RequestBody C createDto) {
        return ResponseEntity.ok(ApiResponse.success(getService().create(createDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<L>> update(@PathVariable Long id, @RequestBody U updateDto) {
        return ResponseEntity.ok(ApiResponse.success(getService().update(id, updateDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<L>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(getService().findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<L>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(getService().findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        getService().deleteLogical(id);
        return ResponseEntity.noContent().build();
    }
}
