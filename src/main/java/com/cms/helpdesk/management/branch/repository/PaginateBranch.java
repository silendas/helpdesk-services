package com.cms.helpdesk.management.branch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.branch.model.Branch;

@Repository
public interface PaginateBranch extends JpaRepository<Branch, Long>, JpaSpecificationExecutor<Branch> {

}
