package com.quedate.service.visit;

import com.quedate.dto.visit.VisitCreateDTO;
import com.quedate.dto.visit.VisitResponseDTO;
import com.quedate.dto.visit.VisitStatusUpdateDTO;
import com.quedate.entity.RentalRequest;
import com.quedate.entity.Visit;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.entity.enums.VisitStatus;
import com.quedate.event.VisitScheduledEvent;
import com.quedate.exception.InvalidScheduleException;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.VisitRepository;
import com.quedate.security.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final RentalRequestRepository rentalRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    public VisitService(
            VisitRepository visitRepository,
            RentalRequestRepository rentalRequestRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.visitRepository = visitRepository;
        this.rentalRequestRepository = rentalRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    public VisitResponseDTO schedule(
            UserPrincipal actor,
            Long rentalRequestId,
            VisitCreateDTO dto
    ) {
        RentalRequest request = rentalRequestRepository.findById(rentalRequestId)
                .orElseThrow(() ->
                        new InvalidScheduleException("Rental request not found"));

        if (!isActorInvolved(actor, request)) {
            throw new AccessDeniedException("Access denied");
        }

        if (request.getStatus() != RentalRequestStatus.PENDING
                && request.getStatus() != RentalRequestStatus.CONFIRMED) {
            throw new InvalidScheduleException(
                    "Visit cannot be scheduled for this rental request"
            );
        }

        Visit visit = Visit.builder()
                .scheduledAt(dto.getScheduledAt())
                .notes(dto.getNotes())
                .status(VisitStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .rentalRequest(request)
                .build();

        return toResponseDTO(
                visitRepository.save(visit)
        );
    }

    public VisitResponseDTO updateStatus(
            UserPrincipal actor,
            Long visitId,
            VisitStatusUpdateDTO dto
    ) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() ->
                        new InvalidScheduleException("Visit not found"));

        RentalRequest request = visit.getRentalRequest();

        if (!isActorInvolved(actor, request)) {
            throw new AccessDeniedException("Access denied");
        }

        VisitStatus current = visit.getStatus();
        VisitStatus next = dto.getStatus();

        boolean validTransition =
                (current == VisitStatus.PENDING
                        && next == VisitStatus.CONFIRMED)
                        ||
                        (current == VisitStatus.CONFIRMED
                                && (next == VisitStatus.COMPLETED
                                || next == VisitStatus.NO_SHOW));

        if (!validTransition) {
            throw new InvalidScheduleException(
                    "Invalid visit status transition"
            );
        }

        /*
         * COMPLETED y NO_SHOW corresponden al estudiante.
         */
        if ((next == VisitStatus.COMPLETED
                || next == VisitStatus.NO_SHOW)
                && (request.getStudent() == null
                || request.getStudent().getUser() == null
                || !request.getStudent()
                .getUser()
                .getId()
                .equals(actor.getUserId()))) {
            throw new AccessDeniedException(
                    "Only the student can complete the visit"
            );
        }

        visit.setStatus(next);

        Visit saved = visitRepository.save(visit);

        if (next == VisitStatus.CONFIRMED) {
            eventPublisher.publishEvent(
                    new VisitScheduledEvent(saved)
            );
        }

        return toResponseDTO(saved);
    }

    public List<VisitResponseDTO> getByRequest(
            UserPrincipal actor,
            Long rentalRequestId
    ) {
        RentalRequest request = rentalRequestRepository.findById(rentalRequestId)
                .orElseThrow(() ->
                        new InvalidScheduleException("Rental request not found"));

        if (!isActorInvolved(actor, request)) {
            throw new AccessDeniedException("Access denied");
        }

        return visitRepository.findByRentalRequest_Id(rentalRequestId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private boolean isActorInvolved(
            UserPrincipal actor,
            RentalRequest request
    ) {
        boolean isStudent =
                request.getStudent() != null
                        && request.getStudent().getUser() != null
                        && request.getStudent()
                        .getUser()
                        .getId()
                        .equals(actor.getUserId());

        boolean isLandlord =
                request.getRoom() != null
                        && request.getRoom().getOwner() != null
                        && request.getRoom().getOwner().getUser() != null
                        && request.getRoom()
                        .getOwner()
                        .getUser()
                        .getId()
                        .equals(actor.getUserId());

        return isStudent || isLandlord;
    }

    private VisitResponseDTO toResponseDTO(Visit visit) {
        return VisitResponseDTO.builder()
                .id(visit.getId())
                .scheduledAt(visit.getScheduledAt())
                .notes(visit.getNotes())
                .status(visit.getStatus())
                .rentalRequestId(
                        visit.getRentalRequest().getId()
                )
                .createdAt(visit.getCreatedAt())
                .build();
    }
}