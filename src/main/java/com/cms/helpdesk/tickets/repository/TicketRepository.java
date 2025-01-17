package com.cms.helpdesk.tickets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.helpdesk.tickets.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    @Query("SELECT t.ticketNumber FROM Ticket t WHERE t.ticketNumber LIKE :datePattern ORDER BY t.id DESC")
    List<String> findLastTicketNumberByDate(@Param("datePattern") String datePattern);
}
