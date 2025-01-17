package com.cms.helpdesk.management.targetcompletion.model;

import com.cms.helpdesk.common.model.BaseEntity;
import com.cms.helpdesk.enums.targetcompletion.TimeIntervalEnum;

import jakarta.persistence.*;
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
@Table(name = "target_completion")
public class TargetCompletion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_interval")
    private TimeIntervalEnum timeInterval;
}
