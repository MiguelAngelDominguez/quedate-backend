package com.quedate.repository;

import com.quedate.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByRentalRequest_Id(Long rentalRequestId);

    List<Visit> findByRentalRequest_Student_Id(Long studentId);
}