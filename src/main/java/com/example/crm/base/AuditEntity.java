package com.example.crm.base;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * The abstract class AuditEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Deprecated
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AuditEntity {

    /** The created by . */
    @CreatedBy
    private String createdBy;

    /** The created at. */
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    /** The modified by. */
    @LastModifiedBy
    private String modifiedBy;

    /** The modified at. */
    @LastModifiedDate
    private LocalDateTime modifiedAt;


}
