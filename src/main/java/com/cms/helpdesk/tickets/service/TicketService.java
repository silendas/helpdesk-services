package com.cms.helpdesk.tickets.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.common.reuse.PageConvert;
import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.branch.repository.BranchRepository;
import com.cms.helpdesk.management.constraintcategory.model.ConstraintCategory;
import com.cms.helpdesk.management.constraintcategory.repository.ConstraintCategoryRepository;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.departments.repository.DepartmentRepository;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.repository.RegionRepository;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.tickets.dto.CreateTicketDTO;
import com.cms.helpdesk.tickets.model.Ticket;
import com.cms.helpdesk.tickets.repository.PaginateTicket;
import com.cms.helpdesk.tickets.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaginateTicket paginate;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ConstraintCategoryRepository constraintRepository;

    public ResponseEntity<Object> getTickets(int page, int size) {
        Specification<Ticket> spec = Specification.where(new Filter<Ticket>().isNotDeleted())
                .and(new Filter<Ticket>().orderByIdDesc());

        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // if (getUser.getLevel().getCode().equals("C")) {

        System.out.println(getUser.getRole());
        Page<Ticket> res = paginate.findAll(spec, PageRequest.of(page, size));
        return Response.buildResponse(
                new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                        Message.SUCCESSFULLY_DEFAULT.getMessage(), PageConvert.convert(res), res.getContent(), null),
                1);
    }

    public ResponseEntity<Object> createTicket(CreateTicketDTO dto) {

        User getUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println(getUser.getRole());

        // Get the current date in YYYMMDD format
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDateStr = today.format(formatter);

        String datePattern = "#" + currentDateStr + "-%";

        String lastTicketNumber = ticketRepository.findLastTicketNumberByDate(datePattern);

        int nextNum = 1;
        if (lastTicketNumber != null) {
            String[] parts = lastTicketNumber.split("-");
            if (parts.length == 2 && parts[0].equals("#" + currentDateStr)) {
                nextNum = Integer.parseInt(parts[1] + 1);
            }
        }

        String newTicketNumber = "#" + currentDateStr + "-" + nextNum;

        Long departmentId, regionId, branchId;

        Ticket ticket = new Ticket();
        ticket.setTicketNumber(newTicketNumber);
        ticket.setDescription(dto.getDescription());
        ticket.setStatus(StatusEnum.OPEN);
        ticket.setExternal(false);

        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, ticketRepository.save(ticket), null), 0);
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket Not Found"));
    }

    public Department getDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department Not Found"));
    }

    public Region getRegion(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region Not Found"));
    }

    public Branch getBranch(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch Not Found"));
    }

    public ConstraintCategory getConstraint(Long id) {
        return constraintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constraint Not Found"));
    }

}
