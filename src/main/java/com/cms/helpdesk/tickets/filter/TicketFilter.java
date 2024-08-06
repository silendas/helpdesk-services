package com.cms.helpdesk.tickets.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.tickets.model.TicketDisposition;

public class TicketFilter {

    public Specification<TicketDisposition> findByTicketId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null)
                return null;
            return criteriaBuilder.equal(root.get("ticket").get("id"), id);
        };
    }

}
