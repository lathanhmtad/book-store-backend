package com.app.repository;

import com.app.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractBaseRepo<T extends BaseEntity> extends JpaRepository<T, Long> {
}
