package com.cms.helpdesk.tickets.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.enums.PriorityEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.tickets.model.Ticket;
import com.cms.helpdesk.tickets.model.TicketDisposition;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import java.util.List;

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

    public Specification<Ticket> findTicketHelpdesk(PriorityEnum priority, Department departmentId, String nip) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
            subquery.select(subRoot.get("ticket").get("id"));
            subquery.where(criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));

            Predicate departmentPredicate = criteriaBuilder.equal(root.get("departmentId"), departmentId);

            Predicate constraintDepartmentPriorityPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("constraintCategoryId").get("departmentId"), departmentId),
                    criteriaBuilder.equal(root.get("constraintCategoryId").get("priority"), priority));

            Predicate createdByPredicate = criteriaBuilder.equal(root.get("createdBy"), nip);

            return criteriaBuilder.or(
                    departmentPredicate,
                    constraintDepartmentPriorityPredicate,
                    criteriaBuilder.in(root.get("id")).value(subquery), createdByPredicate);
        };
    }

    public Specification<Ticket> findTicketSupervisor(List<PriorityEnum> priorities, Region region, Branch branch,
            String nip) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
            subquery.select(subRoot.get("ticket").get("id"));
            subquery.where(criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));

            Predicate priorityPredicate = root.get("constraintCategoryId").get("priority").in(priorities);
            Predicate branchPredicate = criteriaBuilder.equal(root.get("branchId").get("region"), region);
            Predicate regionPredicate = criteriaBuilder.equal(root.get("regionId"), region);

            Predicate createdByPredicate = criteriaBuilder.equal(root.get("createdBy"), nip);

            return criteriaBuilder.or(
                    criteriaBuilder.and(priorityPredicate, criteriaBuilder.or(branchPredicate, regionPredicate)),
                    criteriaBuilder.in(root.get("id")).value(subquery), createdByPredicate);
        };
    }

    /*
     * Helpdesk
     * 1. jika departmentId == user->departmendId dan priority HIGH atau
     * ConstraintCategory->departmentId == user->departmentId dan priority HIGH maka
     * tiket
     * muncul
     * 2. atau ticketDisposition->userTo == user->nip maka tiket muncul note:(tidak
     * mandang priority)
     */

    /*
     * SUPERVISOR
     * 1. jika ConstraintCategory(LOW/MEDIUM) maka muncul berdasarkan
     * ticket->regionId == user->regionId atau ticket->branchId->regionId ==
     * user->regionId
     * 2. atau ticketDisposition->userTo == user->nip maka ticket muncul note:(tidak
     * mandang priority)
     */

}
