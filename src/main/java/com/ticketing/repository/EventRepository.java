package com.ticketing.repository;

import com.ticketing.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    boolean existsByName(String name);


//    Mengambil daftar event dengan pagination berdasarkan kriteria pencarian opsional.
//    Pencarian kata kunci mengabaikan huruf besar/kecil dan berlaku untuk nama atau deskripsi event.

    @Query("SELECT e FROM Event e WHERE " +
           "(:keyword IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR e.category = :category) " +
           "AND (:status IS NULL OR e.status = :status)")
    Page<Event> findEventsWithFilter(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("status") String status,
            Pageable pageable);
}
