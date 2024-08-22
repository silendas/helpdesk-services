package com.cms.helpdesk.tickets.dto;

import com.cms.helpdesk.attachments.dto.AttachmentRes;
import com.cms.helpdesk.enums.targetcompletion.TimeIntervalEnum;
import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.constraintcategory.model.ConstraintCategory;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.regions.model.Region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailRes {

    private Long id;
    private String ticketNumber;
    private Region region;
    private Branch branch;
    private Department department;
    private ConstraintCategory constraintCategory;
    private String priority;
    private String Description;
    private StatusEnum status;
    private Date targetCompletion;
    private EstimateCompletion estimateCompletion;
    private Date timeCompletion;
    private String descriptionCompletion;
    private String processBy;
    private Date processAt;
    private String requesterNip;
    private String requesterEmail;
    private boolean isExternal;
    private String createdBy;
    private Date createdAt;

    private List<TicketDispositionRes> disposition;
    private List<AttachmentRes> attachment;

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EstimateCompletion {
        private String value;
        private TimeIntervalEnum timeInterval;
    }
}
