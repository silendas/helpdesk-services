package com.cms.helpdesk.management.users.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.management.users.dto.request.RegisterDto;
import com.cms.helpdesk.management.users.dto.request.SendOtpDto;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.Registration;
import com.cms.helpdesk.management.users.repository.RegistrationRepository;

@Service
public class RegisterService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    public ResponseEntity<Object> register(RegisterDto dto) {
        List<String> errors = new ArrayList<>();
        Employee employee = employeeService.getEmployeeByNip(dto.getNip());
        if (employee.isRegistered()) {
            errors.add("Karyawan sudah mendaftar sebelumnya");
            return Response.buildResponse(new GlobalDto(302, null,
                    "Karyawan telah terdaftar", null, null, errors), 1);
        }
        Registration regis = new Registration();
        regis.setNip(dto.getNip());
        regis.setEmail(dto.getEmail());
        regis.setPassword(passwordEncoder.encode(dto.getPassword()));
        regis.setName(dto.getName());
        regis.setPhone(dto.getPhone());
        registrationRepository.save(regis);
        return otpService.sendOtp(new SendOtpDto(dto.getEmail()));
    }

}
