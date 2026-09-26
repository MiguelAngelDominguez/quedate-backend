package com.quedate.service.review;

import com.quedate.dto.review.ReviewCreateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.RentalRequest;
import com.quedate.entity.Role;
import com.quedate.entity.Room;
import com.quedate.entity.Student;
import com.quedate.entity.User;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.entity.enums.RoleName;
import com.quedate.exception.DuplicateResourceException;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.ReviewRepository;
import com.quedate.repository.StudentRepository;
import com.quedate.repository.VisitRepository;
import com.quedate.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private RentalRequestRepository rentalRequestRepository;
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ReviewService reviewService;

    private UserPrincipal studentPrincipal;
    private Student student;
    private Room room;

    @BeforeEach
    void setUp() {
        studentPrincipal = principal(3L, RoleName.USER);
        student = new Student();
        student.setId(7L);
        User studentUser = new User();
        studentUser.setId(3L);
        student.setUser(studentUser);

        Landlord landlord = new Landlord();
        landlord.setId(1L);
        User landlordUser = new User();
        landlordUser.setId(2L);
        landlord.setUser(landlordUser);

        room = new Room();
        room.setId(10L);
        room.setTitle("Test Room");
        room.setPrice(new BigDecimal("500"));
        room.setOwner(landlord);
    }

    private RentalRequest confirmedRequest() {
        return RentalRequest.builder()
                .status(RentalRequestStatus.CONFIRMED)
                .student(student)
                .room(room)
                .build();
    }

    @Test
    void create_withConfirmedRequest_createsReview() {
        ReviewCreateDTO dto = new ReviewCreateDTO();
        dto.setRating(5);
        dto.setComment("Excelente");

        when(studentRepository.findByUserId(3L)).thenReturn(Optional.of(student));
        when(reviewRepository.existsByStudent_IdAndRoom_Id(any(), any())).thenReturn(false);
        when(rentalRequestRepository.findByStudent_Id(7L))
                .thenReturn(List.of(confirmedRequest()));
        when(visitRepository.findByRentalRequest_Student_Id(7L))
                .thenReturn(Collections.emptyList());
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = reviewService.create(studentPrincipal, 10L, dto);

        assertEquals(5, result.getRating());
        assertEquals("Excelente", result.getComment());
        verify(reviewRepository).save(any());
    }

    @Test
    void create_withoutExperience_denied() {
        ReviewCreateDTO dto = new ReviewCreateDTO();
        dto.setRating(4);

        when(studentRepository.findByUserId(3L)).thenReturn(Optional.of(student));
        when(reviewRepository.existsByStudent_IdAndRoom_Id(any(), any())).thenReturn(false);
        when(rentalRequestRepository.findByStudent_Id(7L))
                .thenReturn(Collections.emptyList());
        when(visitRepository.findByRentalRequest_Student_Id(7L))
                .thenReturn(Collections.emptyList());

        assertThrows(AccessDeniedException.class,
                () -> reviewService.create(studentPrincipal, 10L, dto));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void create_duplicateReview_rejected() {
        ReviewCreateDTO dto = new ReviewCreateDTO();
        dto.setRating(4);

        when(studentRepository.findByUserId(3L)).thenReturn(Optional.of(student));
        when(reviewRepository.existsByStudent_IdAndRoom_Id(any(), any())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> reviewService.create(studentPrincipal, 10L, dto));
    }

    @Test
    void getLandlordRating_withoutReviews_returnsZero() {
        when(reviewRepository.findAverageRatingByLandlordId(1L)).thenReturn(null);
        when(reviewRepository.findAll()).thenReturn(Collections.emptyList());

        var result = reviewService.getLandlordRating(1L);

        assertEquals(0.0, result.getAverage());
        assertEquals(0L, result.getCount());
    }

    private UserPrincipal principal(Long userId, RoleName... names) {
        User user = new User();
        user.setId(userId);
        Set<Role> roles = new HashSet<>();
        for (RoleName name : names) {
            Role role = new Role();
            role.setName(name);
            roles.add(role);
        }
        user.setRoles(roles);
        return new UserPrincipal(user);
    }
}