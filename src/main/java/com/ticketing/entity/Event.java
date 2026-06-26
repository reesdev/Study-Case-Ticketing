package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String category; // Misal: Konser, Seminar, Transportasi

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Double price;

    // Status: Aktif, Berlangsung, Selesai
    @Column(nullable = false)
    private String status;
}
