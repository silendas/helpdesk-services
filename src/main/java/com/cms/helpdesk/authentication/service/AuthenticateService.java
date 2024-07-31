package com.cms.helpdesk.authentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.helpdesk.authentication.dto.AuthDto;
import com.cms.helpdesk.common.exception.ResourceNotFoundException;
import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.config.jwt.JwtService;
import com.cms.helpdesk.management.users.model.Employee;
import com.cms.helpdesk.management.users.model.User;
import com.cms.helpdesk.management.users.repository.UserRepository;
import com.cms.helpdesk.management.users.service.EmployeeService;

@Service
public class AuthenticateService {

        @Autowired
        private UserRepository repo;

        @Autowired
        private JwtService jwtService;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        public EmployeeService employeeService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public ResponseEntity<Object> authenticate(AuthDto dto) {
                User user = repo.findByEmailOrNip(dto.getUsername(), dto.getUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Email atau NIP tidak ditemukan"));
                List<String> detail = new ArrayList<>();
                if (!user.isApprove()) {
                    detail.add("Akun anda belum di approve");
                    return Response.buildResponse(new GlobalDto(Message.FAILED_LOGIN.getStatusCode(), null,
                            Message.FAILED_LOGIN.getMessage(), null, null, detail), 1);
                }
                if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                    detail.add("Password anda salah");
                    return Response.buildResponse(new GlobalDto(Message.FAILED_LOGIN.getStatusCode(), null,
                            Message.FAILED_LOGIN.getMessage(), null, null, detail), 1);
                }
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getNip(),
                                dto.getPassword()));
                var jwtToken = jwtService.generateToken(user);
                return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_LOGIN.getStatusCode(), jwtToken,
                        Message.SUCCESSFULLY_LOGIN.getMessage(), null, null, null), 2);
            }

        public ResponseEntity<Object> logout(String token) {
                jwtService.blacklistToken(token);
                return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_LOGOUT.getStatusCode(), null,
                                Message.SUCCESSFULLY_LOGOUT.getMessage(), null, null, null), 0);
        }

        public ResponseEntity<Object> nipExist(String nip) {
                Employee employee = employeeService.getEmployeeByNip(nip);
                return Response.buildResponse(new GlobalDto(Message.SUCCESSFULLY_DEFAULT.getStatusCode(), null,
                                Message.SUCCESSFULLY_DEFAULT.getMessage(), null, employee, null), 1);
        }

}
