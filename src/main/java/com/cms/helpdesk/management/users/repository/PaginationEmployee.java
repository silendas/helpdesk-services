package com.cms.helpdesk.management.users.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.users.model.Employee;

@Repository
public interface PaginationEmployee extends PagingAndSortingRepository<Employee, String>, JpaSpecificationExecutor<Employee> {
    
}
