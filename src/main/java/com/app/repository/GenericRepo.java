package com.app.repository;

import com.app.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenericRepo<T extends BaseEntity> extends JpaRepository<T, Long> {
}
