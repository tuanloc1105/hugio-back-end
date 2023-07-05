package vn.com.hugio.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.slf4j.MDC;
import vn.com.hugio.common.constant.CommonConstant;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active = true;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(CommonConstant.DEFAULT_TIMEZONE)).toLocalDateTime();

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(CommonConstant.DEFAULT_TIMEZONE)).toLocalDateTime();

    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    public BaseEntity() {
    }

    public BaseEntity(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(CommonConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        this.updatedAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(CommonConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        this.createdBy = MDC.get("username");
        this.updatedBy = MDC.get("username");
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of(CommonConstant.DEFAULT_TIMEZONE)).toLocalDateTime();
        this.updatedBy = MDC.get("username");
    }
}
