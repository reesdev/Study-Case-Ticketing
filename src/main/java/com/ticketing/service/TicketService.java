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

import java.time.LocalDateTime;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

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
        return TicketResponse.fromEntity(ticket);
    }

    public Page<TicketResponse> getUserTickets(String email, int page, int limit) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User tidak ditemukan"));

        Pageable pageable = PageRequest.of(page, limit);
        Page<Ticket> tickets = ticketRepository.findByUserId(user.getId(), pageable);
        return tickets.map(TicketResponse::fromEntity);
    }

    public Page<TicketResponse> getAllTickets(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Ticket> tickets = ticketRepository.findAll(pageable);
        return tickets.map(TicketResponse::fromEntity);
    }

    public TicketResponse cancelTicket(Long ticketId, String email, String role) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new CustomException("Tiket tidak ditemukan"));

        // Validasi: User hanya bisa membatalkan tiketnya sendiri, Admin bebas
        if (!"ADMIN".equals(role)) {
            if (!ticket.getUser().getEmail().equals(email)) {
                throw new CustomException("Anda tidak berhak membatalkan tiket ini");
            }
        }

        if ("CANCELLED".equals(ticket.getStatus())) {
            throw new CustomException("Tiket sudah dibatalkan sebelumnya");
        }

        ticket.setStatus("CANCELLED");
        ticket = ticketRepository.save(ticket);
        return TicketResponse.fromEntity(ticket);
    }
}
