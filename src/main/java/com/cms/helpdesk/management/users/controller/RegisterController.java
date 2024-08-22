package com.cms.helpdesk.management.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.users.dto.request.RegisterDto;
import com.cms.helpdesk.management.users.service.EmployeeService;
import com.cms.helpdesk.management.users.service.RegisterService;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService service;

    @Autowired
    private EmployeeService employeeService;
 
    @RequestMapping(value = BasePath.BASE_USERS + "/register", method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@Valid @RequestBody RegisterDto dto) {
        return service.register(dto);
    }

    @RequestMapping(value = BasePath.BASE_EMPLOYEE + "/validate", method = RequestMethod.GET)
    public ResponseEntity<Object> getEmployeeByNip(
            @RequestParam("nip") String nip) {
        return employeeService.getEmployeeByNIP(nip);
    }
    
}
