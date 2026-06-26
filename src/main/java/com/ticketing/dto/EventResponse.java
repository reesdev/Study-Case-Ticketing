package com.ticketing.dto;

import com.ticketing.entity.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer capacity;
    private Double price;
    private String status;

    public static EventResponse fromEntity(Event event) {
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
}
