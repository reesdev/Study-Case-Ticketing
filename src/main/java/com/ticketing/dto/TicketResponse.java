package com.ticketing.dto;

import com.ticketing.entity.Ticket;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketResponse {
    private Long id;
    private String eventName;
    private String userName;
    private LocalDateTime purchaseDate;
    private String status;

    public static TicketResponse fromEntity(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setEventName(ticket.getEvent().getName());
        response.setUserName(ticket.getUser().getName());
        response.setPurchaseDate(ticket.getPurchaseDate());
        response.setStatus(ticket.getStatus());
        return response;
    }
}
