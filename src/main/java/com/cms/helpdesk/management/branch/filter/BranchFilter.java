package com.cms.helpdesk.management.branch.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.management.branch.model.Branch;

public class BranchFilter {
    
      public Specification<Branch> byRegionId(Long regionId) {
        return (root, query, criteriaBuilder) -> {
            if (regionId == null) return null;
            return criteriaBuilder.equal(root.get("region").get("id"), regionId);
        };
    }

}
