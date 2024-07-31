package com.cms.helpdesk.tickets.model;

import com.cms.helpdesk.common.model.BaseEntity;
import com.cms.helpdesk.enums.tickets.StatusEnum;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.constraintcategory.model.ConstraintCategory;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.regions.model.Region;
import com.cms.helpdesk.management.users.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department departmentId;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = true)
    private Region regionId;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = true)
    private Branch branchId;

    @ManyToOne
    @JoinColumn(name = "constraint_category_id", nullable = true)
    private ConstraintCategory constraintCategoryId;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "target_completion", nullable = true)
    private Date targetCompletion;

    @Column(name = "time_completion", nullable = true)
    private Date timeCompletion;

    @Column(name = "description_completion", columnDefinition = "TEXT", nullable = true)
    private String descriptionCompletion;

    @ManyToOne
    @JoinColumn(name = "process_by", nullable = true)
    private User processBy;

    @Column(name = "process_at", nullable = true)
    private Date processAt;

    @Column(name = "requester_nip", nullable = true)
    private String requesterNip;

    @Column(name = "requester_email", nullable = true)
    private String requesterEmail;

    @Column(name = "is_external", nullable = true)
    private boolean isExternal;
}
