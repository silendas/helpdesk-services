package com.cms.helpdesk.common.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

@Component
public class JwtException implements AuthenticationEntryPoint {

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        List<String> details = new ArrayList<>();
        details.add("should be login");

        GlobalDto dto = new GlobalDto();
        dto.setStatus(HttpStatus.UNAUTHORIZED.value());
        dto.setMessage("Anda belum login");
        dto.setDetails(details);

        ResponseEntity<Object> responseEntity = Response.buildResponse(dto, 3);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseEntity.getBody());
    }
}
