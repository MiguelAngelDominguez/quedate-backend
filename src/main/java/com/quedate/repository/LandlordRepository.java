package com.quedate.repository;

import com.quedate.entity.Landlord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandlordRepository extends JpaRepository<Landlord, Long> {

    Optional<Landlord> findByUserId(Long userId);

    boolean existsByDni(String dni);
}