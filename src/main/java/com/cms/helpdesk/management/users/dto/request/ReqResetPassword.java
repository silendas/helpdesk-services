package com.cms.helpdesk.management.users.dto.request;

import lombok.Data;

@Data
public class ReqResetPassword {

    private String nip;
    private String password;
    
}
