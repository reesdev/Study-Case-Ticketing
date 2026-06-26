package com.ticketing.repository;

import com.ticketing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Derived query untuk mencari user berdasarkan email (kebutuhan login & validasi)
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
