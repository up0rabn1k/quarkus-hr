package org.acme.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

import static jakarta.persistence.GenerationType.SEQUENCE;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    public Long id;

    @CreationTimestamp
    public Instant createdAt;

    @UpdateTimestamp
    public Instant updatedAt;

    @Version
    @Column(name = "version")
    public Long version;
}
