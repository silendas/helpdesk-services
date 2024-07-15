package com.cms.helpdesk.common.model;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    public Timestamp getCreatedAt() {
        if (createdAt != null) {
            LocalDateTime currentDateTime = createdAt.toLocalDateTime();
            ZoneId utcZone = ZoneId.of("UTC");
            ZoneId wibZone = ZoneId.of("Asia/Jakarta");
            ZonedDateTime utcDateTime = currentDateTime.atZone(utcZone);
            ZonedDateTime wibDateTime = utcDateTime.withZoneSameInstant(wibZone);
            LocalDateTime wibLocalDateTime = wibDateTime.toLocalDateTime();
            return Timestamp.valueOf(wibLocalDateTime);
        }
        return null;
    }

    public Timestamp getUpdatedAt() {
        if (updatedAt != null) {
            LocalDateTime currentDateTime = updatedAt.toLocalDateTime();
            ZoneId utcZone = ZoneId.of("UTC");
            ZoneId wibZone = ZoneId.of("Asia/Jakarta");
            ZonedDateTime utcDateTime = currentDateTime.atZone(utcZone);
            ZonedDateTime wibDateTime = utcDateTime.withZoneSameInstant(wibZone);
            LocalDateTime wibLocalDateTime = wibDateTime.toLocalDateTime();
            return Timestamp.valueOf(wibLocalDateTime);
        }
        return null;
    }

    public Timestamp getDeletedAt() {
        if (deletedAt != null) {
            LocalDateTime currentDateTime = deletedAt.toLocalDateTime();
            ZoneId utcZone = ZoneId.of("UTC");
            ZoneId wibZone = ZoneId.of("Asia/Jakarta");
            ZonedDateTime utcDateTime = currentDateTime.atZone(utcZone);
            ZonedDateTime wibDateTime = utcDateTime.withZoneSameInstant(wibZone);
            LocalDateTime wibLocalDateTime = wibDateTime.toLocalDateTime();
            return Timestamp.valueOf(wibLocalDateTime);
        }
        return null;
    }

    @PrePersist
    public void onPrePersist() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());

        String currentUserName = getCurrentUserName();
        this.createdBy = currentUserName;
        this.updatedBy = currentUserName;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
        this.updatedBy = getCurrentUserName();
    }

    @PreRemove
    public void onPreRemove() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
        this.deletedBy = getCurrentUserName();
        this.isDeleted = true;
    }

    private String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null; // Handle cases where there is no authentication context
    }
}
