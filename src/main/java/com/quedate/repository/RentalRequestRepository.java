package com.quedate.repository;

import com.quedate.entity.RentalRequest;
import com.quedate.entity.enums.RentalRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RentalRequestRepository
        extends JpaRepository<RentalRequest, Long> {

    List<RentalRequest> findByStudent_Id(Long studentId);

    List<RentalRequest> findByRoom_Owner_Id(Long ownerId);

    boolean existsByStudent_IdAndRoom_IdAndStatusIn(
            Long studentId,
            Long roomId,
            Set<RentalRequestStatus> statuses
    );

    List<RentalRequest> findByRoom_IdAndStatus(
            Long roomId,
            RentalRequestStatus status
    );
}