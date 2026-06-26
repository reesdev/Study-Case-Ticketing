package com.ticketing.service;

import com.ticketing.dto.EventRequest;
import com.ticketing.dto.EventResponse;
import com.ticketing.entity.Event;
import com.ticketing.exception.CustomException;
import com.ticketing.repository.EventRepository;
import com.ticketing.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private EventResponse convertToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        response.setCategory(event.getCategory());
        response.setCapacity(event.getCapacity());
        response.setPrice(event.getPrice());
        response.setStatus(event.getStatus());
        return response;
    }

    public EventResponse createEvent(EventRequest request) {
        if (eventRepository.existsByName(request.getName())) {
            throw new CustomException("Nama event sudah digunakan");
        }

        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setCapacity(request.getCapacity());
        event.setPrice(request.getPrice());
        event.setStatus(request.getStatus());

        event = eventRepository.save(event);
        return convertToResponse(event);
    }

    public Page<EventResponse> getAllEvents(String keyword, String category, String status, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Event> eventPage = eventRepository.findEventsWithFilter(keyword, category, status, pageable);
        return eventPage.map(this::convertToResponse);
    }

    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException("Event tidak ditemukan"));

        if ("Berlangsung".equalsIgnoreCase(event.getStatus())) {
            throw new CustomException("Event yang sudah berlangsung tidak bisa diubah");
        }

        if (!event.getName().equals(request.getName()) && eventRepository.existsByName(request.getName())) {
            throw new CustomException("Nama event sudah digunakan");
        }

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setCapacity(request.getCapacity());
        event.setPrice(request.getPrice());
        event.setStatus(request.getStatus());

        event = eventRepository.save(event);
        return convertToResponse(event);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException("Event tidak ditemukan"));

        long bookedTickets = ticketRepository.countByEventIdAndStatus(id, "BOOKED");
        if (bookedTickets > 0) {
            throw new CustomException("Event dengan tiket yang sudah terjual tidak bisa dihapus");
        }

        eventRepository.delete(event);
    }
}
