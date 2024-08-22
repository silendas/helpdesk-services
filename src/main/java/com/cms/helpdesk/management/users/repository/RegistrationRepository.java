package com.cms.helpdesk.management.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.users.model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long>, JpaSpecificationExecutor<Registration> {
    long countByOtp(String otp);

    @Query("SELECT r FROM Registration r WHERE r.nip = ?1 ORDER BY r.id DESC LIMIT 1")
    Optional<Registration> findByNipLimit1Desc(String nip);

    @Query("SELECT r FROM Registration r WHERE r.email = ?1 ORDER BY r.id DESC LIMIT 1")
    Registration findByEmailLimitDesc(String email);
}
