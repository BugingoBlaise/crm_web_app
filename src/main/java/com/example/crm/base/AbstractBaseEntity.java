package com.example.crm.base;

import com.example.crm.customer.domain.EEntityLifeCycle;
 import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * The abstract class AbstractBaseEntity.
 *
 * @author Blaise Mugisha
 * @version 1.0
 * @version 2.0
 */
@Deprecated
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractBaseEntity extends AuditEntity{

    /** The id. */
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    /** The state. */
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EEntityLifeCycle state= EEntityLifeCycle.ACTIVE;
}
