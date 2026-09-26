package com.quedate.service.request;

import com.quedate.dto.request.RentalRequestCreateDTO;
import com.quedate.dto.request.RequestStatusUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.RentalRequest;
import com.quedate.entity.Role;
import com.quedate.entity.Room;
import com.quedate.entity.RoomStatus;
import com.quedate.entity.Student;
import com.quedate.entity.User;
import com.quedate.entity.enums.RentalRequestStatus;
import com.quedate.entity.enums.RoleName;
import com.quedate.exception.DuplicateResourceException;
import com.quedate.exception.ForbiddenException;
import com.quedate.exception.InvalidOperationException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.RentalRequestRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.repository.StudentRepository;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalRequestServiceTest {

    @Mock
    private RentalRequestRepository rentalRequestRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private LandlordRepository landlordRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RentalRequestService rentalRequestService;

    private UserPrincipal student;
    private Student studentEntity;
    private Room room;

    @BeforeEach
    void setUp() {
        student = principal(3L, RoleName.USER);
        studentEntity = new Student();
        studentEntity.setId(7L);
        User studentUser = new User();
        studentUser.setId(3L);
        studentEntity.setUser(studentUser);

        Landlord landlord = new Landlord();
        landlord.setId(1L);
        User landlordUser = new User();
        landlordUser.setId(2L);
        landlord.setUser(landlordUser);

        room = new Room();
        room.setId(10L);
        room.setTitle("Test Room");
        room.setPrice(new BigDecimal("500"));
        room.setStatus(RoomStatus.AVAILABLE);
        room.setOwner(landlord);
    }

    @Test
    void create_persistsPendingRequestAndPublishesEvent() {
        RentalRequestCreateDTO dto = new RentalRequestCreateDTO();
        dto.setMessage("Interesado");
        dto.setStartDate(LocalDate.of(2026, 10, 1));
        dto.setEndDate(LocalDate.of(2027, 3, 1));

        when(studentRepository.findByUserId(3L)).thenReturn(Optional.of(studentEntity));
        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
        when(rentalRequestRepository.existsByStudent_IdAndRoom_IdAndStatusIn(
                any(), any(), any())).thenReturn(false);
        when(rentalRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = rentalRequestService.create(student, 10L, dto);

        assertEquals(RentalRequestStatus.PENDING, result.getStatus());
        assertEquals("Interesado", result.getMessage());
        verify(eventPublisher).publishEvent(any());
    }

    @Test
    void create_rejectsUnavailableRoom() {
        room.setStatus(RoomStatus.RENTED);
        RentalRequestCreateDTO dto = new RentalRequestCreateDTO();

        when(studentRepository.findByUserId(3L)).thenReturn(Optional.of(studentEntity));
        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));

        assertThrows(InvalidOperationException.class,
                () -> rentalRequestService.create(student, 10L, dto));
    }

    @Test
    void create_rejectsDuplicateRequest() {
        RentalRequestCreateDTO dto = new RentalRequestCreateDTO();

        when(studentRepository.findByUserId(3L)).thenReturn(Optional.of(studentEntity));
        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
        when(rentalRequestRepository.existsByStudent_IdAndRoom_IdAndStatusIn(
                any(), any(), any())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> rentalRequestService.create(student, 10L, dto));
    }

    @Test
    void updateStatus_ownerLandlordCanConfirm() {
        RentalRequest request = RentalRequest.builder()
                .status(RentalRequestStatus.PENDING)
                .student(studentEntity)
                .room(room)
                .build();
        RequestStatusUpdateDTO dto = new RequestStatusUpdateDTO();
        dto.setStatus(RentalRequestStatus.CONFIRMED);

        when(rentalRequestRepository.findById(5L)).thenReturn(Optional.of(request));
        when(rentalRequestRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = rentalRequestService.updateStatus(
                principal(2L, RoleName.LANDLORD), 5L, dto);

        assertEquals(RentalRequestStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void updateStatus_foreignUserIsDenied() {
        RentalRequest request = RentalRequest.builder()
                .status(RentalRequestStatus.PENDING)
                .student(studentEntity)
                .room(room)
                .build();
        RequestStatusUpdateDTO dto = new RequestStatusUpdateDTO();
        dto.setStatus(RentalRequestStatus.CONFIRMED);

        when(rentalRequestRepository.findById(5L)).thenReturn(Optional.of(request));

        assertThrows(AccessDeniedException.class,
                () -> rentalRequestService.updateStatus(student, 5L, dto));
    }

    @Test
    void updateStatus_invalidTransitionRejected() {
        RentalRequest request = RentalRequest.builder()
                .status(RentalRequestStatus.CONFIRMED)
                .student(studentEntity)
                .room(room)
                .build();
        RequestStatusUpdateDTO dto = new RequestStatusUpdateDTO();
        dto.setStatus(RentalRequestStatus.PENDING);

        when(rentalRequestRepository.findById(5L)).thenReturn(Optional.of(request));

        assertThrows(InvalidOperationException.class,
                () -> rentalRequestService.updateStatus(
                        principal(2L, RoleName.LANDLORD), 5L, dto));
    }

    @Test
    void create_withoutStudentProfileRejected() {
        when(studentRepository.findByUserId(3L)).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class,
                () -> rentalRequestService.create(student, 10L, new RentalRequestCreateDTO()));
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