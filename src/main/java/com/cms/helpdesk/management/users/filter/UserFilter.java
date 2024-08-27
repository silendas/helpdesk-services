package com.cms.helpdesk.management.users.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.management.roles.model.Role;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

public class UserFilter {

    public Specification<User> approval(String approval) {
        return (root, query, criteriaBuilder) -> {
            if (approval.equals(""))
                return null;
            boolean isApproved = Boolean.parseBoolean(approval);
            return criteriaBuilder.equal(root.get("isApprove"), isApproved);
        };
    }

    public Specification<User> notIncludeSuperadmin() {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.notEqual(root.get("id"), 1);
        };
    }

    public Specification<User> role(Long roleId) {
        return (root, query, criteriaBuilder) -> {
            if (roleId == null || roleId == 0)
                return null;
            return criteriaBuilder.equal(root.get("role").get("id"), roleId);
        };
    }

    public Specification<User> department(Long departmentId) {
        return (root, query, criteriaBuilder) -> {
            if (departmentId == null || departmentId == 0)
                return null;
            return criteriaBuilder.equal(root.get("employee").get("department").get("id"), departmentId);
        };
    }

    public Specification<User> query(String search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";

                Join<User, Employee> employeeJoin = root.join("employee");

                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(employeeJoin.get("name")),
                        searchPattern);
                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                        searchPattern);
                Predicate nipPredicate = criteriaBuilder.like(criteriaBuilder.lower(employeeJoin.get("nip")),
                        searchPattern);

                predicates.add(criteriaBuilder.or(namePredicate, emailPredicate, nipPredicate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
