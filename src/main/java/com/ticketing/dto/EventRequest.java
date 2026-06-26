package com.ticketing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequest {

    @NotBlank(message = "Nama event tidak boleh kosong")
    private String name;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    private String description;
    
    @NotBlank(message = "Kategori tidak boleh kosong")
    private String category;

    @NotNull(message = "Kapasitas tidak boleh kosong")
    @Min(value = 0, message = "Kapasitas tidak boleh negatif")
    private Integer capacity;

    @NotNull(message = "Harga tidak boleh kosong")
    @Min(value = 0, message = "Harga tidak boleh negatif")
    private Double price;

    @NotBlank(message = "Status tidak boleh kosong")
    private String status;
}
