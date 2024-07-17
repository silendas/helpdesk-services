package com.cms.helpdesk.management.roles.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.roles.model.Role;

@Repository
public interface PaginateRole extends PagingAndSortingRepository<Role, Long>, JpaSpecificationExecutor<Role> {

}
