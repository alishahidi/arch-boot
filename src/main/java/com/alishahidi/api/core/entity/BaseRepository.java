package com.alishahidi.api.core.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
    List<T> findByDeletedAtIsNull();

    Optional<T> findByIdAndDeletedAtIsNull(Long id);
}