package com.cms.helpdesk.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.authentication.dto.AuthDto;
import com.cms.helpdesk.authentication.service.AuthenticateService;
import com.cms.helpdesk.common.path.BasePath;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticateService service;
    
    @PostMapping(value = BasePath.BASE_AUTHENTICATE)
    public ResponseEntity<Object> authenticate(@RequestBody AuthDto dto) {
        return service.authenticate(dto);
    }

    @PostMapping(value = BasePath.BASE_LOGOUT)
    public ResponseEntity<Object> logout(@NonNull HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        String jwtToken = tokenHeader.substring(7);
        return service.logout(jwtToken);
    }
    
}
