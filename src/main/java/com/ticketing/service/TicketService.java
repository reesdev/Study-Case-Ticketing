package com.ticketing.service;

import com.ticketing.dto.TicketRequest;
import com.ticketing.dto.TicketResponse;
import com.ticketing.entity.Event;
import com.ticketing.entity.Ticket;
import com.ticketing.entity.User;
import com.ticketing.exception.CustomException;
import com.ticketing.repository.EventRepository;
import com.ticketing.repository.TicketRepository;
import com.ticketing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setEventName(ticket.getEvent().getName());
        response.setUserName(ticket.getUser().getName());
        response.setPurchaseDate(ticket.getPurchaseDate());
        response.setStatus(ticket.getStatus());
        return response;
    }

    @Transactional
    public TicketResponse bookTicket(String email, TicketRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User tidak ditemukan"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new CustomException("Event tidak ditemukan"));

        if (!"Tersedia".equalsIgnoreCase(event.getStatus()) && !"Aktif".equalsIgnoreCase(event.getStatus())) {
            throw new CustomException("Tiket untuk event ini tidak tersedia");
        }

        long bookedCount = ticketRepository.countByEventIdAndStatus(event.getId(), "BOOKED");
        if (bookedCount >= event.getCapacity()) {
            throw new CustomException("Kapasitas event sudah penuh");
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setStatus("BOOKED");

        ticket = ticketRepository.save(ticket);
        return convertToResponse(ticket);
    }

    public Page<TicketResponse> getTickets(String email, int page, int limit, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User tidak ditemukan"));

        Pageable pageable = PageRequest.of(page, limit);
        Page<Ticket> tickets;
        
        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(23, 59, 59) : null;
        
        if ("ADMIN".equals(user.getRole())) {
            tickets = ticketRepository.findTicketsWithFilter(null, start, end, pageable);
        } else {
            tickets = ticketRepository.findTicketsWithFilter(user.getId(), start, end, pageable);
        }
        
        return tickets.map(this::convertToResponse);
    }

    public TicketResponse getTicketById(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CustomException("Tiket tidak ditemukan"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User tidak ditemukan"));

        // Validasi: User hanya bisa melihat tiketnya sendiri, Admin bebas
        if (!"ADMIN".equals(user.getRole())) {
            if (!ticket.getUser().getEmail().equals(email)) {
                throw new CustomException("Anda tidak berhak melihat tiket ini");
            }
        }

        return convertToResponse(ticket);
    }

    public TicketResponse cancelTicket(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CustomException("Tiket tidak ditemukan"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User tidak ditemukan"));

        // Validasi: User hanya bisa membatalkan tiketnya sendiri, Admin bebas
        if (!"ADMIN".equals(user.getRole())) {
            if (!ticket.getUser().getEmail().equals(email)) {
                throw new CustomException("Anda tidak berhak membatalkan tiket ini");
            }
        }

        if ("CANCELLED".equals(ticket.getStatus())) {
            throw new CustomException("Tiket sudah dibatalkan sebelumnya");
        }

        ticket.setStatus("CANCELLED");
        ticket = ticketRepository.save(ticket);
        return convertToResponse(ticket);
    }
}
