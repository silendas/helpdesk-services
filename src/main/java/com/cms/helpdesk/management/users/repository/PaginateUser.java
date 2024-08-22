package com.cms.helpdesk.management.users.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.users.model.User;

@Repository
public interface PaginateUser extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

}
