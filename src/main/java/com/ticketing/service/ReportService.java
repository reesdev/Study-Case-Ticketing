package com.ticketing.service;

import com.ticketing.dto.ReportSummaryResponse;
import com.ticketing.dto.TicketResponse;
import com.ticketing.entity.Ticket;
import com.ticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private TicketRepository ticketRepository;

    public ReportSummaryResponse getSummaryReport() {
        List<Ticket> allTickets = ticketRepository.findAll();
        
        long totalTicketsSold = 0;
        double totalRevenue = 0.0;
        
        // Loop klasik sesuai aturan Anti-AI
        for (int i = 0; i < allTickets.size(); i++) {
            Ticket ticket = allTickets.get(i);
            if ("BOOKED".equals(ticket.getStatus())) {
                totalTicketsSold++;
                totalRevenue += ticket.getEvent().getPrice();
            }
        }
        
        return new ReportSummaryResponse(totalTicketsSold, totalRevenue);
    }

    public List<TicketResponse> getEventReport(Long eventId) {
        List<Ticket> tickets = ticketRepository.findByEventId(eventId);
        List<TicketResponse> responses = new ArrayList<>();
        
        // Loop klasik
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            TicketResponse response = new TicketResponse();
            response.setId(ticket.getId());
            response.setEventName(ticket.getEvent().getName());
            response.setUserName(ticket.getUser().getName());
            response.setPurchaseDate(ticket.getPurchaseDate());
            response.setStatus(ticket.getStatus());
            responses.add(response);
        }
        
        return responses;
    }
}
