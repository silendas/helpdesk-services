package com.cms.helpdesk.common.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;

import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
public abstract class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getDefaultMessage());
        }

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 1);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);

    }
}
