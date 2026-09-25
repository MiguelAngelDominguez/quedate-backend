package com.quedate.service.request;

import com.quedate.dto.request.RentalRequestCreateDTO;
import com.quedate.dto.request.RentalRequestResponseDTO;
import com.quedate.dto.request.RequestStatusUpdateDTO;
import com.quedate.entity.*;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.event.RentalRequestCreatedEvent;
import com.quedate.exception.DuplicateResourceException;
import com.quedate.exception.InvalidOperationException;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.RoomRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final RoomRepository roomRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RentalRequestService(
            RentalRequestRepository rentalRequestRepository,
            RoomRepository roomRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.roomRepository = roomRepository;
        this.eventPublisher = eventPublisher;
    }

    public RentalRequestResponseDTO create(
            Student student,
            Long roomId,
            RentalRequestCreateDTO dto
    ) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new InvalidOperationException("Room is not available");
        }

        boolean duplicate =
                rentalRequestRepository.existsByStudent_IdAndRoom_IdAndStatusIn(
                        student.getId(),
                        roomId,
                        Set.of(
                                RentalRequestStatus.PENDING,
                                RentalRequestStatus.CONFIRMED
                        )
                );

        if (duplicate) {
            throw new DuplicateResourceException(
                    "An active rental request already exists"
            );
        }

        RentalRequest request = RentalRequest.builder()
                .message(dto.getMessage())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(RentalRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .student(student)
                .room(room)
                .build();

        RentalRequest saved = rentalRequestRepository.save(request);

        eventPublisher.publishEvent(
                new RentalRequestCreatedEvent(saved)
        );

        return toResponseDTO(saved);
    }

    public RentalRequestResponseDTO updateStatus(
            User actor,
            Long id,
            RequestStatusUpdateDTO dto
    ) {
        RentalRequest request = rentalRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental request not found"));

        boolean isOwner =
                request.getRoom().getOwner().getId().equals(actor.getId());

        boolean isAdmin =
                actor.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Access denied");
        }

        RentalRequestStatus current = request.getStatus();
        RentalRequestStatus next = dto.getStatus();

        boolean validTransition =
                (current == RentalRequestStatus.PENDING
                        && (next == RentalRequestStatus.CONFIRMED
                        || next == RentalRequestStatus.REJECTED))
                        ||
                        (current == RentalRequestStatus.CONFIRMED
                                && next == RentalRequestStatus.CANCELLED);

        if (!validTransition) {
            throw new InvalidOperationException(
                    "Invalid rental request status transition"
            );
        }

        request.setStatus(next);

        return toResponseDTO(
                rentalRequestRepository.save(request)
        );
    }

    public List<RentalRequestResponseDTO> getMyRequests(Student student) {
        return rentalRequestRepository.findByStudent_Id(student.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<RentalRequestResponseDTO> getLandlordRequests(
            Landlord landlord
    ) {
        return rentalRequestRepository.findByRoom_Owner_Id(landlord.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private RentalRequestResponseDTO toResponseDTO(
            RentalRequest request
    ) {
        return RentalRequestResponseDTO.builder()
                .id(request.getId())
                .message(request.getMessage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .studentId(request.getStudent().getId())
                .roomId(request.getRoom().getId())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
