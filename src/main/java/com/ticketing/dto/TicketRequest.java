package com.ticketing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {

    @NotNull(message = "ID Event tidak boleh kosong")
    private Long eventId;
}
