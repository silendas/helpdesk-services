package com.cms.helpdesk.management.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.users.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @Query("SELECT e FROM Employee e WHERE e.nip = ?1")
    public Optional<Employee> findByNip(String nip);

}
