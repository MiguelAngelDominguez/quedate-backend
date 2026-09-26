package com.quedate.repository;

import com.quedate.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findByOwner_Id(Long ownerId, Pageable pageable);

    @Query("""
        SELECT r
        FROM Room r
        WHERE r.status = com.quedate.entity.RoomStatus.AVAILABLE
          AND (:universityId IS NULL OR r.university.id = :universityId)
          AND (:district IS NULL OR LOWER(r.location.district) = LOWER(:district))
          AND (:minPrice IS NULL OR r.price >= :minPrice)
          AND (:maxPrice IS NULL OR r.price <= :maxPrice)
        """)
    Page<Room> search(
            @Param("universityId") Long universityId,
            @Param("district") String district,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}