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

}
