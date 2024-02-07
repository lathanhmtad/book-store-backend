package com.app.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;

import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    @CreatedBy
    @Column(updatable = false)
    protected U createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    protected Date createdDate;

    @LastModifiedBy
    protected U lastModifiedBy;

    @UpdateTimestamp
    protected Date lastModifiedDate;
}