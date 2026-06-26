package com.ticketing.controller;

import com.ticketing.dto.TicketRequest;
import com.ticketing.dto.TicketResponse;
import com.ticketing.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
@Tag(name = "3. Tickets", description = "Pembelian & Pembatalan Tiket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> bookTicket(@Valid @RequestBody TicketRequest request, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.bookTicket(email, request));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        
        String email = authentication.getName();
        
        Page<TicketResponse> tickets = ticketService.getTickets(email, page, limit, startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", tickets.getContent());
        response.put("current_page", tickets.getNumber());
        response.put("total_pages", tickets.getTotalPages());
        response.put("total_items", tickets.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(ticketService.getTicketById(id, email));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponse> cancelTicket(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        
        return ResponseEntity.ok(ticketService.cancelTicket(id, email));
    }
}
