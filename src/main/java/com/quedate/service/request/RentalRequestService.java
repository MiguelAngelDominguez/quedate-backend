package com.quedate.service.request;

import com.quedate.dto.request.RentalRequestCreateDTO;
import com.quedate.dto.request.RentalRequestResponseDTO;
import com.quedate.dto.request.RequestStatusUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.RentalRequest;
import com.quedate.entity.Room;
import com.quedate.entity.RoomStatus;
import com.quedate.entity.Student;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.event.RentalRequestCreatedEvent;
import com.quedate.exception.DuplicateResourceException;
import com.quedate.exception.ForbiddenException;
import com.quedate.exception.InvalidOperationException;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.repository.StudentRepository;
import com.quedate.security.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final RoomRepository roomRepository;
    private final StudentRepository studentRepository;
    private final LandlordRepository landlordRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RentalRequestService(
            RentalRequestRepository rentalRequestRepository,
            RoomRepository roomRepository,
            StudentRepository studentRepository,
            LandlordRepository landlordRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
        this.landlordRepository = landlordRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public RentalRequestResponseDTO create(
            UserPrincipal principal,
            Long roomId,
            RentalRequestCreateDTO dto
    ) {
        Student student = studentRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Student profile not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomId));

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

    @Transactional
    public RentalRequestResponseDTO updateStatus(
            UserPrincipal actor,
            Long id,
            RequestStatusUpdateDTO dto
    ) {
        RentalRequest request = rentalRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental request not found: " + id));

        boolean isOwner =
                request.getRoom().getOwner() != null
                        && request.getRoom().getOwner().getUser() != null
                        && request.getRoom()
                        .getOwner()
                        .getUser()
                        .getId()
                        .equals(actor.getUserId());

        boolean isAdmin = actor.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Access denied");
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

    @Transactional(readOnly = true)
    public List<RentalRequestResponseDTO> getMyRequests(
            UserPrincipal principal
    ) {
        Student student = studentRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Student profile not found"));

        return rentalRequestRepository.findByStudent_Id(student.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalRequestResponseDTO> getLandlordRequests(
            UserPrincipal principal
    ) {
        Landlord landlord = landlordRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Landlord profile not found"));

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