package com.cms.helpdesk.management.roles.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.management.roles.model.Role;

public class RoleFilter {

    public Specification<Role> notIncludeSuperadmin() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.notEqual(root.get("id"), 1);
        };
    }

}
