package com.cms.helpdesk.tickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.tickets.model.Ticket;

@Repository
public interface PaginateTicket extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

}
