package com.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events")
@org.hibernate.annotations.SQLDelete(sql = "UPDATE events SET is_deleted = true WHERE id=?")
@org.hibernate.annotations.SQLRestriction("is_deleted = false")
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

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}
