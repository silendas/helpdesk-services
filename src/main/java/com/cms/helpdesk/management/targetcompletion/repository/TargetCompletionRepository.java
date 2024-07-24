package com.cms.helpdesk.management.targetcompletion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.targetcompletion.model.TargetCompletion;

@Repository
public interface TargetCompletionRepository
        extends JpaRepository<TargetCompletion, Long>, JpaSpecificationExecutor<TargetCompletion> {

}
