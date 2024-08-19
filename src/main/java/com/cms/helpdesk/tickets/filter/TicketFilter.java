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

    // public Specification<Ticket> findTicketSupervisor(List<PriorityEnum>
    // priorities, Region regionId, Branch branchId,
    // String nip) {
    // return (root, query, criteriaBuilder) -> {
    // // Subquery to fetch tickets with matching user_to
    // Subquery<Long> subquery = query.subquery(Long.class);
    // Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
    // subquery.select(subRoot.get("ticket").get("id"));
    // subquery.where(
    // criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));

    // return criteriaBuilder.or(
    // root.get("constraintCategoryId").get("priority").in(priorities),
    // criteriaBuilder.and(
    // criteriaBuilder.equal(root.get("regionId"), regionId),
    // criteriaBuilder.equal(root.get("branchId"), branchId),
    // criteriaBuilder.in(root.get("id")).value(subquery)));
    // };
    // }

    // public Specification<Ticket> findTicketSupervisor(List<PriorityEnum>
    // priorities, Region region, Branch branch,
    // String nip) {
    // return (root, query, criteriaBuilder) -> {
    // // Predicate for tickets with priority LOW or MEDIUM
    // Predicate priorityPredicate =
    // root.get("constraintCategoryId").get("priority").in(priorities);

    // // Conditional predicates for region and branch
    // Predicate branchPredicate = criteriaBuilder.conjunction(); // default to true
    // if (branch != null) {
    // branchPredicate = criteriaBuilder.equal(root.get("branchId"), branch);
    // }

    // Predicate regionPredicate = criteriaBuilder.conjunction(); // default to true
    // if (region != null) {
    // regionPredicate = criteriaBuilder.equal(root.get("regionId"), region);
    // }

    // // Subquery to find tickets disposed to the supervisor (user_to)
    // Predicate userToPredicate = criteriaBuilder.conjunction(); // default to true
    // if (nip != null) {
    // Subquery<Long> subquery = query.subquery(Long.class);
    // Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
    // subquery.select(subRoot.get("ticket").get("id"));
    // subquery.where(criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));
    // userToPredicate = criteriaBuilder.in(root.get("id")).value(subquery);
    // }

    // // Combine all predicates
    // return criteriaBuilder.and(
    // priorityPredicate,
    // criteriaBuilder.or(branchPredicate, regionPredicate, userToPredicate));
    // };
    // }

    // Bisa nih
    // public Specification<Ticket> findTicketSupervisor(List<PriorityEnum>
    // priorities, Region region, Branch branch,
    // String nip) {
    // return (root, query, criteriaBuilder) -> {
    // Subquery<Long> subquery = query.subquery(Long.class);
    // Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
    // subquery.select(subRoot.get("ticket").get("id"));
    // subquery.where(criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));

    // Predicate priorityPredicate =
    // root.get("constraintCategoryId").get("priority").in(priorities);

    // Predicate branchPredicate = criteriaBuilder.equal(root.get("branchId"),
    // branch);
    // Predicate regionPredicate = criteriaBuilder.equal(root.get("regionId"),
    // region);

    // return criteriaBuilder.and(
    // priorityPredicate,
    // criteriaBuilder.or(branchPredicate, regionPredicate,
    // criteriaBuilder.in(root.get("id")).value(subquery)));
    // };
    // }

    // public Specification<Ticket> findTicketHelpdesk(PriorityEnum priority,
    // Department departmentId, String nip) {
    // return (root, query, criteriaBuilder) -> {
    // Subquery<Long> subquery = query.subquery(Long.class);
    // Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
    // subquery.select(subRoot.get("ticket").get("id"));
    // subquery.where(
    // criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));

    // return criteriaBuilder.and(
    // criteriaBuilder.equal(root.get("constraintCategoryId").get("priority"),
    // priority),
    // criteriaBuilder.equal(root.get("departmentId"), departmentId),
    // criteriaBuilder.or(
    // criteriaBuilder.equal(root.get("constraintCategoryId").get("departmentId"),
    // departmentId),
    // criteriaBuilder.in(root.get("id")).value(subquery)));
    // };
    // }

    public Specification<Ticket> findTicketHelpdesk(PriorityEnum priority, Department departmentId, String nip) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<TicketDisposition> subRoot = subquery.from(TicketDisposition.class);
            subquery.select(subRoot.get("ticket").get("id"));
            subquery.where(criteriaBuilder.equal(subRoot.get("userTo").get("nip"), nip));

            Predicate departmentPriorityPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("departmentId"), departmentId),
                    criteriaBuilder.equal(root.get("constraintCategoryId").get("priority"), priority));

            Predicate constraintDepartmentPriorityPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("constraintCategoryId").get("departmentId"), departmentId),
                    criteriaBuilder.equal(root.get("constraintCategoryId").get("priority"), priority));

            Predicate createdByPredicate = criteriaBuilder.equal(root.get("createdBy"), nip);

            return criteriaBuilder.or(
                    departmentPriorityPredicate,
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
                    criteriaBuilder.in(root.get("id")).value(subquery));
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
