package com.cms.helpdesk.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.tickets.model.Ticket;

@Repository
public interface DashboardRepo extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    
}
