package com.cms.helpdesk.tickets.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.tickets.dto.CloseTicketDTO;
import com.cms.helpdesk.tickets.dto.CreateTicketDTO;
import com.cms.helpdesk.tickets.dto.ProcessTicketDTO;
import com.cms.helpdesk.tickets.dto.TicketDispositionDTO;
import com.cms.helpdesk.tickets.service.TicketService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = BasePath.BASE_TICKETS)
public class TicketController {

    @Autowired
    private TicketService service;

    @GetMapping
    public ResponseEntity<Object> getTickets(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        return service.getTickets(page.orElse(0), size.orElse(10));
    }

    @GetMapping("/report")
    public ResponseEntity<InputStreamResource> getReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end) {
        return service.downloadReport(start.orElse(null), end.orElse(null));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<Object> findTicket(@PathVariable("id") Long id) {
        return service.findTicket(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createTicket(@Valid @RequestBody CreateTicketDTO dto) {
        return service.createTicket(dto);
    }

    @PatchMapping("/{id}/disposition")
    public ResponseEntity<Object> disposiitonTicket(@PathVariable("id") Long id,
            @RequestBody TicketDispositionDTO dto) {
        return service.dispositionTicket(id, dto);
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<Object> processTicket(@PathVariable("id") Long id, ProcessTicketDTO dto) {
        return service.processTicket(id, dto);
    }

    @PatchMapping("/{id}/closed")
    public ResponseEntity<Object> closedTicket(@PathVariable("id") Long id, @RequestBody CloseTicketDTO dto) {
        return service.closedTicket(id, dto);
    }

}
