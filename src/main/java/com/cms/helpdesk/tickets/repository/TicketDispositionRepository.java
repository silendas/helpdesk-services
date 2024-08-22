package com.cms.helpdesk.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.tickets.model.TicketDisposition;

@Repository
public interface TicketDispositionRepository
        extends JpaRepository<TicketDisposition, Long>, JpaSpecificationExecutor<TicketDisposition> {

}
