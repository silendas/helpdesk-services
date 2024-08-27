package com.cms.helpdesk.management.departments.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.management.departments.model.Department;

public class DepartementFilter {
 
    public Specification<Department> byId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null)
                return null;
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }
    
}
