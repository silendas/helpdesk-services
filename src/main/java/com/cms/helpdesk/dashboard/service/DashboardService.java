package com.cms.helpdesk.dashboard.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.Filter;
import com.cms.helpdesk.dashboard.dto.ResponseDashboard;
import com.cms.helpdesk.dashboard.dto.Separate.TicketOfficeChart;
import com.cms.helpdesk.dashboard.dto.Separate.TicketConstraint;
import com.cms.helpdesk.dashboard.dto.Separate.TicketMontlyChart;
import com.cms.helpdesk.dashboard.dto.Separate.TicketStatusChart;
import com.cms.helpdesk.dashboard.repository.DashboardRepo;
import com.cms.helpdesk.enums.PriorityEnum;
import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.branch.service.BranchService;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.regions.service.RegionService;
import com.cms.helpdesk.tickets.model.Ticket;

@Service
public class DashboardService {

    @Autowired
    private DashboardRepo repo;

    @Autowired
    private BranchService branchService;

    @Autowired
    private RegionService regionService;

    public ResponseEntity<Object> builderResponseDashboard(Optional<Date> starDate, Optional<Date> endDate) {
        List<Ticket> getTickets = getTicketByFilter(starDate.orElse(getStartOfYear()), endDate.orElse(getEndOfYear()));
        return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, buildDashboard(getTickets), null), 1);
    }

    public ResponseDashboard buildDashboard(List<Ticket> tickets) {
        ResponseDashboard res = new ResponseDashboard();
        res.setTicketConstraints(dashboardTicketConstraint(tickets));
        res.setTicketStatusPieCharts(dashboardTicketStatusPieChart(tickets));
        res.setTicketMontlyCharts(dashboardMonthlyChart(tickets));
        res.setTicketRegionCharts(dashboardTicketRegionChart(tickets));
        res.setTicketBranchCharts(dashboardTicketBranchChart(tickets));
        return res;
    }

    public List<TicketMontlyChart> dashboardMonthlyChart(List<Ticket> tickets) {
        Map<String, Map<StatusEnum, Long>> monthlyStatusMap = new LinkedHashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("id", "ID"));
        String[] monthNames = { "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember" };
        for (String month : monthNames) {
            monthlyStatusMap.put(month, new HashMap<>());
            for (StatusEnum status : StatusEnum.values()) {
                monthlyStatusMap.get(month).put(status, 0L);
            }
        }
        for (Ticket ticket : tickets) {
            String month = monthFormat.format(ticket.getCreatedAt());
            StatusEnum status = ticket.getStatus();
            Map<StatusEnum, Long> statusMap = monthlyStatusMap.get(month);
            statusMap.put(status, statusMap.get(status) + 1);
        }
        List<TicketMontlyChart> monthlyCharts = new ArrayList<>();
        for (Map.Entry<String, Map<StatusEnum, Long>> entry : monthlyStatusMap.entrySet()) {
            String month = entry.getKey();
            Map<StatusEnum, Long> statusMap = entry.getValue();
            List<TicketStatusChart> ticketStatusCharts = statusMap.entrySet().stream()
                    .map(e -> new TicketStatusChart(e.getKey(), e.getValue(), null))
                    .collect(Collectors.toList());
            TicketMontlyChart monthlyChart = new TicketMontlyChart();
            monthlyChart.setMonth(month);
            monthlyChart.setTicketStatus(ticketStatusCharts);
            monthlyCharts.add(monthlyChart);
        }
        return monthlyCharts;
    }

    public static Date getStartOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public List<TicketOfficeChart> dashboardTicketBranchChart(List<Ticket> tickets) {
        Map<String, TicketOfficeChart> branchMap = new HashMap<>();
        for (Branch branch : branchService.getListBranch()) {
            TicketOfficeChart branchChart = new TicketOfficeChart();
            branchChart.setName(branch.getName());
            branchChart.setTotal(0L);
            branchMap.put(branch.getName(), branchChart);
        }
        for (Ticket ticket : tickets) {
            Branch branch = ticket.getBranchId();
            if (branch != null) {
                TicketOfficeChart branchChart = branchMap.get(branch.getName());
                branchChart.setTotal(branchChart.getTotal() + 1);
            }
        }
        return new ArrayList<>(branchMap.values());
    }

    public List<TicketOfficeChart> dashboardTicketRegionChart(List<Ticket> tickets) {
        Map<String, TicketOfficeChart> regionMap = new HashMap<>();
        for (Region region : regionService.getListRegion()) {
            TicketOfficeChart regionChart = new TicketOfficeChart();
            regionChart.setName(region.getName());
            regionChart.setTotal(0L);
            regionMap.put(region.getName(), regionChart);
        }
        for (Ticket ticket : tickets) {
            Region region = ticket.getRegionId();
            if (region != null) {
                TicketOfficeChart regionChart = regionMap.get(region.getName());
                regionChart.setTotal(regionChart.getTotal() + 1);
            }
        }
        return regionMap.values().stream()
                .sorted((chart1, chart2) -> chart1.getName().compareToIgnoreCase(chart2.getName()))
                .collect(Collectors.toList());
    }
    

    public List<TicketStatusChart> dashboardTicketStatusPieChart(List<Ticket> tickets) {
        Map<StatusEnum, TicketStatusChart> statusMap = new HashMap<>();
        long totalTickets = tickets.size();
        for (StatusEnum status : StatusEnum.values()) {
            TicketStatusChart statusChart = new TicketStatusChart();
            statusChart.setStatus(status);
            statusChart.setTotal(0L);
            statusChart.setPercent(0.0);
            statusMap.put(status, statusChart);
        }
        for (Ticket ticket : tickets) {
            StatusEnum status = ticket.getStatus();
            TicketStatusChart statusChart = statusMap.get(status);
            statusChart.setTotal(statusChart.getTotal() + 1);
        }
        for (TicketStatusChart statusChart : statusMap.values()) {
            double percent = (double) statusChart.getTotal() / totalTickets;
            statusChart.setPercent(percent);
        }

        return new ArrayList<>(statusMap.values());
    }

    public List<TicketConstraint> dashboardTicketConstraint(List<Ticket> tickets) {
        Map<String, TicketConstraint> priorityMap = new HashMap<>();
        long totalTickets = tickets.size();
        TicketConstraint totals = new TicketConstraint();
        totals.setPriority("Total");
        totals.setTotal(totalTickets);
        totals.setPercent(1.0);
        priorityMap.put("Total", totals);
        for (PriorityEnum priority : PriorityEnum.values()) {
            TicketConstraint constraint = new TicketConstraint();
            constraint.setPriority(priority.toString());
            constraint.setTotal(0L);
            constraint.setPercent(0.0);
            priorityMap.put(priority.toString(), constraint);
        }
        for (Ticket ticket : tickets) {
            PriorityEnum priority = ticket.getConstraintCategoryId().getPriority();
            TicketConstraint constraint = priorityMap.get(priority.toString());
            constraint.setTotal(constraint.getTotal() + 1);
        }
        for (TicketConstraint constraint : priorityMap.values()) {
            double percent = (double) constraint.getTotal() / totalTickets;
            constraint.setPercent(percent);
        }
    
        return new ArrayList<>(priorityMap.values());
    }
    

    public List<Ticket> getTicketByFilter(Date starDate, Date endDate) {
        Specification<Ticket> spec = Specification.where(new Filter<Ticket>().createdAtBetween(starDate, endDate));
        return repo.findAll(spec);
    }

}
