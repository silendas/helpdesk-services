package com.cms.helpdesk.common.reuse;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

public class PageConvert {

    public static Object convert(Page data) {
        Map<String, Object> mapping = new LinkedHashMap<>();
        mapping.put("pageNumber", data.getPageable().getPageNumber());
        mapping.put("pageSize", data.getPageable().getPageSize());
        mapping.put("offset", data.getPageable().getOffset());
        mapping.put("totalPages", data.getTotalPages());
        mapping.put("totalElements", data.getTotalElements());
        mapping.put("isFirst", data.isFirst());
        mapping.put("isLast", data.isLast());
        return mapping;
    }

}
