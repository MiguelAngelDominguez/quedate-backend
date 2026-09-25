package com.quedate.repository;

import com.quedate.entity.Verification;
import com.quedate.entity.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationRepository
        extends JpaRepository<Verification, Long> {

    List<Verification> findByStatus(VerificationStatus status);

    List<Verification> findByLandlord_Id(Long landlordId);
}