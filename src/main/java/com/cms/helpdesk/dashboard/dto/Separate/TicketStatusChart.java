package com.cms.helpdesk.dashboard.dto.Separate;

import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusChart {

    private StatusEnum status;

    private Long total;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double percent;

}
