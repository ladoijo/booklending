package com.library.booklending.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private long id;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private OffsetDateTime createdAt;

  @Column(nullable = false)
  @UpdateTimestamp
  private OffsetDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;
}
