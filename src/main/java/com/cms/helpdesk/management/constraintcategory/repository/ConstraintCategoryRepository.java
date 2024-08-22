package com.cms.helpdesk.management.constraintcategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.management.constraintcategory.model.ConstraintCategory;

@Repository
public interface ConstraintCategoryRepository
        extends JpaRepository<ConstraintCategory, Long>, JpaSpecificationExecutor<ConstraintCategory> {

}
