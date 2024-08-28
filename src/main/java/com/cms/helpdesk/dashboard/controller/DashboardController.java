package com.cms.helpdesk.dashboard.controller;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cms.helpdesk.common.path.BasePath;
import com.cms.helpdesk.dashboard.service.DashboardService;

@RestController
@RequestMapping(value = BasePath.BASE_DASHBOARD)
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping
    private ResponseEntity<Object> getDashboard(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> start,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Optional<Date> end) {
        return service.builderResponseDashboard(start, end);
    }

}
