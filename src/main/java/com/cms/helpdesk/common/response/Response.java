package com.cms.helpdesk.common.response;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cms.helpdesk.common.response.dto.GlobalDto;
import com.cms.helpdesk.common.reuse.ConvertDate;

public class Response {

    private static String version = "0.0.1";
    private static String timeStamp = ConvertDate.formatISO8601(new Date());

    public static Object meta(GlobalDto dto) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("version", version);
        response.put("timeStamp", timeStamp);
        response.put("message", dto.getMessage());
        response.put("status", dto.getStatus());
        if (dto.getPageable() != null) {
            response.put("pagination", dto.getPageable());
        }
        return response;
    }

    public static Object successLogin(String token) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("token", token);
        return response;
    }

    public static Object errorDetails(List<String> obj) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", obj);
        return response;
    }

    // Note : action value if 0 = no data, 1 = with data, 2 = login, 3 = exception
    public static ResponseEntity<Object> buildResponse(GlobalDto globalDto, int action) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("meta", meta(globalDto));
        if (action == 1 || action == 3) {
            if (globalDto.getStatus() != 200) {
                response.put("data", errorDetails(globalDto.getDetails()));
            } else {
                response.put("data", globalDto.getData());
            }
        } else if (action == 2) {
            response.put("data", successLogin(globalDto.getToken()));
        }
        if (globalDto.getStatus() == 401 && action == 3) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (globalDto.getStatus() == 404 && action == 3) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else if (globalDto.getStatus() == 400 && action == 3) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (globalDto.getStatus() == 500 && action == 3) {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
