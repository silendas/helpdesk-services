package com.cms.helpdesk.tickets.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.enums.PriorityEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.tickets.model.Ticket;
import com.cms.helpdesk.tickets.model.TicketDisposition;

public class TicketFilter {

    public Specification<TicketDisposition> findByTicketId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null)
                return null;
            return criteriaBuilder.equal(root.get("ticket").get("id"), id);
        };
    }

    public Specification<Ticket> findByCreatedBy(String nip) {
        return (root, query, criteriaBuilder) -> {
            if (nip == null)
                return null;
            return criteriaBuilder.equal(root.get("createdBy"), nip);
        };
    }

    public Specification<Ticket> findByPriorityAndRegionAndBranch(PriorityEnum priority, Region regionId,
            Branch branchId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("priority"), priority),
                criteriaBuilder.equal(root.get("regionId"), regionId),
                criteriaBuilder.equal(root.get("branchId"), branchId));
    }

    public Specification<Ticket> findByPriorityAndDepartment(PriorityEnum priority1, PriorityEnum priority2,
            PriorityEnum priority3,
            Department departmentId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                root.get("constraintCategoryId").get("priority").in(priority1, priority2, priority3),
                criteriaBuilder.equal(root.get("constraintCategoryId").get("departmentId"), departmentId));
    }

    // public Specification<Ticket> findByDispositionUser(Long userId) {
    // return (root, query, criteriaBuilder) -> {
    // if (userId == null) {
    // return null;
    // }
    // // Join dengan tabel Tickets Disposition dan filter berdasarkan user_to
    // return criteriaBuilder.exists(
    // query.subquery(Long.class)
    // .from(TicketDisposition.class)
    // .select(criteriaBuilder.literal(1L))
    // .where(
    // criteriaBuilder.equal(root.get("id"), root.get("ticket_id")),
    // criteriaBuilder.equal(root.get("user_to"), userId)));
    // };
    // }

}
