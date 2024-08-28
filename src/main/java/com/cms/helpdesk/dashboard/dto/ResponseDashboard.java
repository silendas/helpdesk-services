package com.cms.helpdesk.dashboard.dto;

import java.util.List;

import com.cms.helpdesk.dashboard.dto.Separate.TicketOfficeChart;
import com.cms.helpdesk.dashboard.dto.Separate.TicketConstraint;
import com.cms.helpdesk.dashboard.dto.Separate.TicketMontlyChart;
import com.cms.helpdesk.dashboard.dto.Separate.TicketStatusChart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDashboard {

    private List<TicketConstraint> ticketConstraints;

    private List<TicketMontlyChart> ticketMontlyCharts;

    private List<TicketStatusChart> ticketStatusPieCharts;

    private List<TicketOfficeChart> ticketRegionCharts;

    private List<TicketOfficeChart> ticketBranchCharts;
    
}
