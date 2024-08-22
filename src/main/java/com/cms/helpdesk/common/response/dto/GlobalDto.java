package com.cms.helpdesk.common.response.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalDto {
    private int status;
    private String token;
    private String message;
    private Object pageable;
    private Object data;
    private List<String> details;
}
