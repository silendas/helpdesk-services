package com.cms.helpdesk.management.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cms.helpdesk.management.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.employee.nip = :nip")
    Optional<User> findByNip(@Param("nip") String nip);

    @Query("SELECT u FROM User u WHERE u.email = :email or u.employee.nip = :nip ")
    Optional<User> findByEmailOrNip(@Param("email") String email, @Param("nip") String nip);

    @Transactional
    @Modifying
    @Query(value = "update User set password = ?1 where employee.nip = ?2")
    void resetPasswordUser(String newPassword, String nip);

}
