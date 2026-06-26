package com.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportSummaryResponse {
    private long totalTicketsSold;
    private double totalRevenue;
}
