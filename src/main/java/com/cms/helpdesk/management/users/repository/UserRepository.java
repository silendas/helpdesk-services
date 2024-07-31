package com.cms.helpdesk.management.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.nip = :nip")
    Optional<User> findByNip(String nip);

    @Query("SELECT u FROM User u WHERE u.email = :email or u.nip = :nip ")
    Optional<User> findByEmailOrNip(@Param("email") String email, @Param("nip") String nip);


}
