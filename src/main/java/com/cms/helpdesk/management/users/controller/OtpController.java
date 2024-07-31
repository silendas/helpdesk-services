package com.cms.helpdesk.management.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.management.users.dto.request.SendOtpDto;
import com.cms.helpdesk.management.users.dto.request.VerifyOtpDTO;
import com.cms.helpdesk.management.users.service.OtpService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = BasePath.BASE_OTP)
public class OtpController {

    @Autowired
    private OtpService service;

    @PostMapping("/send")
    public ResponseEntity<Object> sendOtp(@Valid @RequestBody SendOtpDto dto) {
        return service.sendOtp(dto);
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateOtp(@Valid @RequestBody VerifyOtpDTO dto) {
        return service.verifyOtp(dto);
    }
    
}
