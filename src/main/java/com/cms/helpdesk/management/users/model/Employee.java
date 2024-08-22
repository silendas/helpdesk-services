package com.cms.helpdesk.management.users.model;

import org.springframework.validation.annotation.Validated;

import com.cms.helpdesk.common.model.BaseEntity;
import com.cms.helpdesk.management.branch.model.Branch;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.regions.model.Region;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity{

    @Id
    @Column(name = "nip", unique = true)
    private String nip;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @JsonIgnore
    @Column(name = "is_registered")
    private boolean registered;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = true)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = true)
    private Branch branch;
    
}
