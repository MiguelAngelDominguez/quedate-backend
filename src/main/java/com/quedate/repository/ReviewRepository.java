package com.quedate.repository;

import com.quedate.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRoom_Id(Long roomId);

    boolean existsByStudent_IdAndRoom_Id(Long studentId, Long roomId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.room.id = :roomId")
    Double findAverageRatingByRoomId(Long roomId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.room.owner.id = :landlordId")
    Double findAverageRatingByLandlordId(Long landlordId);
}