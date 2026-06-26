package com.ticketing.controller;

import com.ticketing.dto.EventRequest;
import com.ticketing.dto.EventResponse;
import com.ticketing.exception.CustomException;
import com.ticketing.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/events")
@Tag(name = "2. Events", description = "Manajemen Event (CRUD & Pencarian)")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getEvents(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Page<EventResponse> events = eventService.getAllEvents(keyword, category, status, page, limit);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", events.getContent());
        response.put("current_page", events.getNumber());
        response.put("total_pages", events.getTotalPages());
        response.put("total_items", events.getTotalElements());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request, Authentication authentication) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest request, Authentication authentication) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEvent(@PathVariable Long id, Authentication authentication) {
        eventService.deleteEvent(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Event berhasil dihapus");
        return ResponseEntity.ok(response);
    }
}
