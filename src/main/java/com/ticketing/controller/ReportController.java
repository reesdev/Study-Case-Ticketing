package com.ticketing.controller;

import com.ticketing.dto.ReportSummaryResponse;
import com.ticketing.dto.TicketResponse;
import com.ticketing.exception.CustomException;
import com.ticketing.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryResponse> getSummary(Authentication authentication) {
        return ResponseEntity.ok(reportService.getSummaryReport());
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<List<TicketResponse>> getEventReport(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(reportService.getEventReport(id));
    }
}
