package com.cms.helpdesk.management.targetcompletion.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.targetcompletion.model.TargetCompletion;

@Repository
public interface PaginateTargetCompletion
        extends PagingAndSortingRepository<TargetCompletion, Long>, JpaSpecificationExecutor<TargetCompletion> {

}
