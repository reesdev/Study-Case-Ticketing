package com.ticketing.repository;

import com.ticketing.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Cari semua tiket berdasarkan event id
    List<Ticket> findByEventId(Long eventId);
    
    // Cari semua tiket berdasarkan user id (dengan pagination)
    Page<Ticket> findByUserId(Long userId, Pageable pageable);
    
    // Menghitung jumlah tiket yang sudah di-booking (untuk validasi kapasitas)
    long countByEventIdAndStatus(Long eventId, String status);
}
