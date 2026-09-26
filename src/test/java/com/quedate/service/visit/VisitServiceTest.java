package com.quedate.service.visit;

import com.quedate.dto.visit.VisitCreateDTO;
import com.quedate.dto.visit.VisitStatusUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.RentalRequest;
import com.quedate.entity.Role;
import com.quedate.entity.Room;
import com.quedate.entity.Student;
import com.quedate.entity.User;
import com.quedate.entity.Visit;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.entity.enums.RoleName;
import com.quedate.entity.enums.VisitStatus;
import com.quedate.exception.InvalidScheduleException;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.VisitRepository;
import com.quedate.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;
    @Mock
    private RentalRequestRepository rentalRequestRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private VisitService visitService;

    private UserPrincipal studentPrincipal;
    private UserPrincipal landlordPrincipal;
    private RentalRequest request;

    @BeforeEach
    void setUp() {
        studentPrincipal = principal(3L, RoleName.USER);
        landlordPrincipal = principal(2L, RoleName.LANDLORD);

        Student student = new Student();
        student.setId(7L);
        User studentUser = new User();
        studentUser.setId(3L);
        student.setUser(studentUser);

        Landlord landlord = new Landlord();
        landlord.setId(1L);
        User landlordUser = new User();
        landlordUser.setId(2L);
        landlord.setUser(landlordUser);

        Room room = new Room();
        room.setId(10L);
        room.setTitle("Test Room");
        room.setPrice(new BigDecimal("500"));
        room.setOwner(landlord);

        request = RentalRequest.builder()
                .id(5L)
                .status(RentalRequestStatus.CONFIRMED)
                .student(student)
                .room(room)
                .build();
    }

    @Test
    void schedule_involvedStudentCanSchedule() {
        VisitCreateDTO dto = new VisitCreateDTO();
        dto.setScheduledAt(LocalDateTime.of(2026, 10, 5, 15, 0));
        dto.setNotes("Primera visita");

        when(rentalRequestRepository.findById(5L)).thenReturn(Optional.of(request));
        when(visitRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = visitService.schedule(studentPrincipal, 5L, dto);

        assertEquals(VisitStatus.PENDING, result.getStatus());
        assertEquals(5L, result.getRentalRequestId());
    }

    @Test
    void schedule_uninvolvedActorDenied() {
        VisitCreateDTO dto = new VisitCreateDTO();
        dto.setScheduledAt(LocalDateTime.of(2026, 10, 5, 15, 0));

        when(rentalRequestRepository.findById(5L)).thenReturn(Optional.of(request));

        assertThrows(AccessDeniedException.class,
                () -> visitService.schedule(
                        principal(99L, RoleName.USER), 5L, dto));
    }

    @Test
    void schedule_rejectedWhenRequestNotPendingOrConfirmed() {
        request.setStatus(RentalRequestStatus.REJECTED);
        VisitCreateDTO dto = new VisitCreateDTO();
        dto.setScheduledAt(LocalDateTime.of(2026, 10, 5, 15, 0));

        when(rentalRequestRepository.findById(5L)).thenReturn(Optional.of(request));

        assertThrows(InvalidScheduleException.class,
                () -> visitService.schedule(studentPrincipal, 5L, dto));
    }

    @Test
    void updateStatus_studentCompletesVisitAfterConfirmation() {
        Visit visit = Visit.builder()
                .status(VisitStatus.CONFIRMED)
                .rentalRequest(request)
                .build();
        VisitStatusUpdateDTO dto = new VisitStatusUpdateDTO();
        dto.setStatus(VisitStatus.COMPLETED);

        when(visitRepository.findById(20L)).thenReturn(Optional.of(visit));
        when(visitRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = visitService.updateStatus(studentPrincipal, 20L, dto);

        assertEquals(VisitStatus.COMPLETED, result.getStatus());
    }

    @Test
    void landlock_cannotCompleteVisit() {
        Visit visit = Visit.builder()
                .status(VisitStatus.CONFIRMED)
                .rentalRequest(request)
                .build();
        VisitStatusUpdateDTO dto = new VisitStatusUpdateDTO();
        dto.setStatus(VisitStatus.COMPLETED);

        when(visitRepository.findById(20L)).thenReturn(Optional.of(visit));

        assertThrows(AccessDeniedException.class,
                () -> visitService.updateStatus(landlordPrincipal, 20L, dto));
    }

    @Test
    void updateStatus_invalidTransitionRejected() {
        Visit visit = Visit.builder()
                .status(VisitStatus.PENDING)
                .rentalRequest(request)
                .build();
        VisitStatusUpdateDTO dto = new VisitStatusUpdateDTO();
        dto.setStatus(VisitStatus.COMPLETED);

        when(visitRepository.findById(20L)).thenReturn(Optional.of(visit));

        assertThrows(InvalidScheduleException.class,
                () -> visitService.updateStatus(studentPrincipal, 20L, dto));
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