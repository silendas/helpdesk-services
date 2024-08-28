package com.cms.helpdesk.dashboard.dto.Separate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketBranchChart {

    private String branch_name;

    private Long total;
    
}
