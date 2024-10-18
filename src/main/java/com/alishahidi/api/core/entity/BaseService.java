package com.alishahidi.api.core.entity;

import com.alishahidi.api.core.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseEntity, C extends BaseDto, U extends BaseDto, L extends BaseDto> {

    protected abstract BaseRepository<T> getRepository();

    protected abstract BaseMapper<T, C, U, L> getMapper();

    @Transactional
    public ApiResponse<L> saveOrUpdate(Long id, U updateDto, C createDto) {
        if (id != null) {
            return update(id, updateDto);
        } else {
            return create(createDto);
        }
    }

    public ApiResponse<L> update(Long id, U updateDto) {
        Optional<T> existingEntityOpt = getRepository().findById(id);

        if (existingEntityOpt.isPresent() && existingEntityOpt.get().getDeletedAt() == null) {
            T existingEntity = existingEntityOpt.get();
            getMapper().update(updateDto, existingEntity);
            existingEntity.setId(id);
            existingEntity.setUpdatedAt(new Date());
            beforeSaveOrUpdate(existingEntity);
            return ApiResponse.success(getMapper().load(getRepository().save(existingEntity)));
        } else {
            throw new EntityNotFoundException("Entity with ID " + id + " not found or has been deleted.");
        }
    }

    public ApiResponse<L> create(C createDto) {
        T entity = getMapper().create(createDto);
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        beforeSaveOrUpdate(entity);
        return ApiResponse.success(getMapper().load(getRepository().save(entity)));
    }

    public void beforeSaveOrUpdate(T entity) {
    }

    @Transactional(readOnly = true)
    public ApiResponse<L> findById(Long id) {
        return ApiResponse.success(getRepository().findByIdAndDeletedAtIsNull(id).map(getMapper()::load).orElseThrow(() -> new EntityNotFoundException("Entity not found")));
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<L>> findAll() {
        return ApiResponse.success(getRepository().findByDeletedAtIsNull().stream().map(getMapper()::load).toList());
    }

    @Transactional
    public void deleteLogical(Long id) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));

        entity.setDeletedAt(new Date());

        getRepository().save(entity);
    }

}
