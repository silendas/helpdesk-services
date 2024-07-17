package com.cms.helpdesk.management.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReqUserDTO {

    @NotBlank(message = "NIP Should Not be Empty")
    private String nip;

    @NotBlank(message = "Name Should Not be Empty")
    private String name;

    @NotBlank(message = "Email Should Not be Empty")
    private String email;

    @NotBlank(message = "Password Should Not be Empty")
    private String password;

    @NotNull(message = "Role Should Not be Empty")
    private Long roleId;

    private Long departmentId = null;
    private Long regionId = null;
    private Long branchId = null;

    // private Long main_id = null;
}
