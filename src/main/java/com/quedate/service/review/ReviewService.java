package com.quedate.service.review;

import com.quedate.dto.rating.RatingSummaryDTO;
import com.quedate.dto.review.ReviewCreateDTO;
import com.quedate.dto.review.ReviewResponseDTO;
import com.quedate.entity.RentalRequest;
import com.quedate.entity.Review;
import com.quedate.entity.Student;
import com.quedate.entity.Visit;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.entity.enums.VisitStatus;
import com.quedate.exception.DuplicateResourceException;
import com.quedate.exception.ForbiddenException;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.ReviewRepository;
import com.quedate.repository.StudentRepository;
import com.quedate.repository.VisitRepository;
import com.quedate.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RentalRequestRepository rentalRequestRepository;
    private final VisitRepository visitRepository;
    private final StudentRepository studentRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            RentalRequestRepository rentalRequestRepository,
            VisitRepository visitRepository,
            StudentRepository studentRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.rentalRequestRepository = rentalRequestRepository;
        this.visitRepository = visitRepository;
        this.studentRepository = studentRepository;
    }

    public ReviewResponseDTO create(
            UserPrincipal principal,
            Long roomId,
            ReviewCreateDTO dto
    ) {
        Student student = studentRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Student profile not found"));

        if (reviewRepository.existsByStudent_IdAndRoom_Id(
                student.getId(),
                roomId
        )) {
            throw new DuplicateResourceException(
                    "Student has already reviewed this room"
            );
        }

        boolean hasConfirmedRequest =
                rentalRequestRepository
                        .findByStudent_Id(student.getId())
                        .stream()
                        .anyMatch(request ->
                                request.getRoom().getId().equals(roomId)
                                        && request.getStatus()
                                        == RentalRequestStatus.CONFIRMED
                        );

        boolean hasCompletedVisit =
                visitRepository
                        .findByRentalRequest_Student_Id(student.getId())
                        .stream()
                        .anyMatch(visit ->
                                visit.getRentalRequest()
                                        .getRoom()
                                        .getId()
                                        .equals(roomId)
                                        && visit.getStatus()
                                        == VisitStatus.COMPLETED
                        );

        if (!hasConfirmedRequest && !hasCompletedVisit) {
            throw new AccessDeniedException(
                    "Student has no completed experience with this room"
            );
        }

        RentalRequest request =
                rentalRequestRepository
                        .findByStudent_Id(student.getId())
                        .stream()
                        .filter(r ->
                                r.getRoom().getId().equals(roomId)
                        )
                        .findFirst()
                        .orElseThrow();

        Review review = Review.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .student(student)
                .room(request.getRoom())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponseDTO(
                reviewRepository.save(review)
        );
    }

    public List<ReviewResponseDTO> getByRoom(Long roomId) {
        return reviewRepository.findByRoom_Id(roomId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public RatingSummaryDTO getLandlordRating(Long landlordId) {
        Double average =
                reviewRepository.findAverageRatingByLandlordId(landlordId);

        long count = reviewRepository.findAll()
                .stream()
                .filter(review ->
                        review.getRoom()
                                .getOwner()
                                .getId()
                                .equals(landlordId)
                )
                .count();

        return new RatingSummaryDTO(
                average == null ? 0.0 : average,
                count
        );
    }

    private ReviewResponseDTO toResponseDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .studentId(review.getStudent().getId())
                .roomId(review.getRoom().getId())
                .createdAt(review.getCreatedAt())
                .build();
    }
}