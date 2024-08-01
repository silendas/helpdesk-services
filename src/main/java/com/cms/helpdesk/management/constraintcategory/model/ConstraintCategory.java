package com.cms.helpdesk.management.constraintcategory.model;

import com.cms.helpdesk.common.model.BaseEntity;
import com.cms.helpdesk.enums.PriorityEnum;
import com.cms.helpdesk.management.departments.model.Department;
import com.cms.helpdesk.management.targetcompletion.model.TargetCompletion;

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

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "constraint_category")
public class ConstraintCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department departmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private PriorityEnum priority;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "target_completion_id")
    private TargetCompletion targetCompletionId;

}
