package com.cms.helpdesk.management.users.dto.response;

import com.cms.helpdesk.management.roles.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {

    private String nip;
    private String name;
    private String email;
    private Role role;
    private Boolean isApprove;
    private OrganizeRes organize;
    
}
