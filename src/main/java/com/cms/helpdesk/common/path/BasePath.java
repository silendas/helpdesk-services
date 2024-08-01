package com.cms.helpdesk.common.path;

public interface BasePath {
    String BASE_API = "/api";

    String BASE_AUTHENTICATE = BASE_API + "/authenticate";
    String BASE_LOGOUT = BASE_API + "/logout";
    String BASE_REGISTER = BASE_API + "/register";
    String BASE_CHECK_NIP = BASE_API + "/nip/validation";

    String BASE_ROLES = BASE_API + "/roles";
    String BASE_DEPARTMENTS = BASE_API + "/departments";
    String BASE_REGIONS = BASE_API + "/regions";
    String BASE_BRANCHS = BASE_API + "/branchs";

    String BASE_USERS = BASE_API + "/users";
    String BASE_EMPLOYEE = BASE_API + "/employee";
    
    String BASE_OTP = BASE_API + "/otp";
    String BASE_FORGOT_PASSWORD = "/forgotpwdui";

    String BASE_CONSTRAINT_CATEGORY = BASE_API + "/constraint-category";
    String BASE_TARGET_COMPLETION = BASE_API + "/target-completion";

    String BASE_TICKETS = BASE_API + "/tickets";

}
