package com.ticketing.repository;

import com.ticketing.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    // Catatan: Menggunakan Custom JPQL Dinamis untuk pemfilteran yang bersifat Null-Safe.
    // Pendekatan ini mencegah NullPointerException (Error 500) yang umumnya terjadi 
    // pada fungsi bawaan JPA (Between) ketika input rentang tanggal bernilai kosong (Null).
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:userId IS NULL OR t.user.id = :userId) AND " +
           "(:startDate IS NULL OR t.purchaseDate >= :startDate) AND " +
           "(:endDate IS NULL OR t.purchaseDate <= :endDate)")
    Page<Ticket> findTicketsWithFilter(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    // Cari semua tiket berdasarkan event id
    List<Ticket> findByEventId(Long eventId);
    
    // Cari semua tiket berdasarkan user id (dengan pagination)
    Page<Ticket> findByUserId(Long userId, Pageable pageable);
    
    // Menghitung jumlah tiket yang sudah di-booking (untuk validasi kapasitas)
    long countByEventIdAndStatus(Long eventId, String status);
}
