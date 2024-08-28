package com.cms.helpdesk.dashboard.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.dashboard.dto.ResponseDashboard;
import com.cms.helpdesk.dashboard.dto.Separate.TicketConstraint;
import com.cms.helpdesk.dashboard.dto.Separate.TicketMontlyChart;
import com.cms.helpdesk.dashboard.dto.Separate.TicketStatusChart;
import com.cms.helpdesk.dashboard.repository.DashboardRepo;
import com.cms.helpdesk.enums.PriorityEnum;
import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.cms.helpdesk.tickets.model.Ticket;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepo repo;

    public ResponseEntity<Object> builderResponseDashboard(Optional<Date> starDate, Optional<Date> endDate) {
        List<Ticket> getTickets = getTicketByFilter(starDate.orElse(null), endDate.orElse(null));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, buildDashboard(getTickets), null), 1);
    }

    public ResponseDashboard buildDashboard(List<Ticket> tickets) {
        ResponseDashboard res = new ResponseDashboard();
        res.setTicketConstraints(dashboardTicketConstraint(tickets));
        res.setTicketStatusPieCharts(dashboardTicketStatusPieChart(tickets));
        res.setTicketMontlyCharts(dashboardMonthlyChart(tickets));
        return res;
    }

    public List<TicketMontlyChart> dashboardMonthlyChart(List<Ticket> tickets) {
        return null;
    }

    public List<TicketStatusChart> dashboardTicketStatusPieChart(List<Ticket> tickets) {
        Map<StatusEnum, TicketStatusChart> statusMap = new HashMap<>();
        for (Ticket ticket : tickets) {
            StatusEnum status = ticket.getStatus();
            TicketStatusChart statusChart = statusMap.get(status);
            if (statusChart == null) {
                statusChart = new TicketStatusChart();
                statusChart.setStatus(status);
                statusChart.setTotal(0L);
                statusMap.put(status, statusChart);
            }
            statusChart.setTotal(statusChart.getTotal() + 1);
        }
        return new ArrayList<>(statusMap.values());
    }

    public List<TicketConstraint> dashboardTicketConstraint(List<Ticket> tickets) {
        Map<PriorityEnum, TicketConstraint> priorityMap = new HashMap<>();
        for (Ticket ticket : tickets) {
            PriorityEnum priority = ticket.getConstraintCategoryId().getPriority();
            TicketConstraint constraint = priorityMap.get(priority);
            if (constraint == null) {
                constraint = new TicketConstraint();
                constraint.setPriority(priority);
                constraint.setTotal(0L);
                priorityMap.put(priority, constraint);
            }
            constraint.setTotal(constraint.getTotal() + 1);
        }
        return new ArrayList<>(priorityMap.values());
    }

    public List<Ticket> getTicketByFilter(Date starDate, Date endDate) {
        Specification<Ticket> spec = Specification.where(new Filter<Ticket>().createdAtBetween(starDate, endDate));
        return repo.findAll(spec);
    }

}
