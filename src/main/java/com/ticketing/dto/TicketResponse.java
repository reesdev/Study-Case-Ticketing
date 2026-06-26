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

}
