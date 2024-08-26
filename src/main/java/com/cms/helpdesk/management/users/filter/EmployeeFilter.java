package com.cms.helpdesk.management.users.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.management.users.model.Employee;

public class EmployeeFilter {
    
      public Specification<Employee> notIncludeSuperadmin() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.notEqual(root.get("nip"), "cmssuperadmin");
        };
    }

}
