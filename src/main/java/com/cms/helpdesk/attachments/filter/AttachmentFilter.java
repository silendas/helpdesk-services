package com.cms.helpdesk.attachments.filter;

import org.springframework.data.jpa.domain.Specification;

import com.cms.helpdesk.attachments.model.Attachment;

public class AttachmentFilter {
    public Specification<Attachment> findByTicketId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null)
                return null;
            return criteriaBuilder.equal(root.get("ticket").get("id"), id);
        };
    }
}
