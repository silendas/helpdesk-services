package com.cms.helpdesk.management.departments.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.departments.model.Department;

@Repository
public interface PaginateDepartment
        extends PagingAndSortingRepository<Department, Long>, JpaSpecificationExecutor<Department> {

}
