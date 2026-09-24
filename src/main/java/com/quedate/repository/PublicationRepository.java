package com.quedate.repository;

import com.quedate.entity.Publication;
import com.quedate.entity.PublicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    Optional<Publication> findByRoom_Id(Long roomId);

    List<Publication> findByStatus(PublicationStatus status);
}