package com.cms.helpdesk.management.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.common.exception.UserFoundException;
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
    private UserService userService;

    @Autowired
    private OtpService otpService;

    public ResponseEntity<Object> register(RegisterDto dto) {
        EmployeeWithValidations(dto);
        Registration regis = new Registration();
        regis.setNip(dto.getNip());
        regis.setEmail(dto.getEmail());
        regis.setPassword(passwordEncoder.encode(dto.getPassword()));
        regis.setName(dto.getName());
        regis.setPhone(dto.getPhone());
        registrationRepository.save(regis);
        return otpService.sendOtp(new SendOtpDto(dto.getEmail()));
    }

    public void EmployeeWithValidations(RegisterDto dto){
        Employee employee = employeeService.getEmployeeByNip(dto.getNip());

        if (employee.isRegistered()) {
            throw new UserFoundException("Karyawan sudah mendaftar sebelumnya");
        } else if(userService.getUserByEmail(dto.getEmail()) != null) {
            throw new UserFoundException("Email sudah terdaftar");
        }

    }

}
