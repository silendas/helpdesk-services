package com.cms.helpdesk.common.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.cms.helpdesk.common.response.Message;
import com.cms.helpdesk.common.response.Response;
import com.cms.helpdesk.common.response.dto.GlobalDto;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_INTERNAL_SERVER_ERROR.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_INTERNAL_SERVER_ERROR.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(NotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_NOT_FOUND.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_NOT_FOUND.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.NOT_FOUND_DEFAULT.getMessage());
        errorDetails.setStatus(Message.NOT_FOUND_DEFAULT.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 1);
    }

    @ExceptionHandler(UserFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(UserFoundException ex,
            WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_ALREADY_EXIST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_ALREADY_EXIST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 1);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Object> handleNullException(NullPointerException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<Object> handleNullException(UsernameNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
        BadCredentialsException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(OtpValidationException.class)
    public final ResponseEntity<Object> handleOtpValidationException(
        OtpValidationException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_ACCESS_DENIED.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_ACCESS_DENIED.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex, WebRequest request) {
        log.error("Database access error: ", ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());

        GlobalDto errorDetails = new GlobalDto();
        errorDetails.setMessage(Message.EXCEPTION_BAD_REQUEST.getMessage());
        errorDetails.setStatus(Message.EXCEPTION_BAD_REQUEST.getStatusCode());
        errorDetails.setDetails(details);

        return Response.buildResponse(errorDetails, 3);
    }

}
