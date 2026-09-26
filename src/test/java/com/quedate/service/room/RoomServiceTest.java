package com.quedate.service.room;

import com.quedate.dto.room.RoomCreateDTO;
import com.quedate.dto.room.RoomUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.Role;
import com.quedate.entity.Room;
import com.quedate.entity.RoomStatus;
import com.quedate.entity.User;
import com.quedate.entity.enums.RoleName;
import com.quedate.exception.ForbiddenException;
import com.quedate.exception.InvalidOperationException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.LocationRepository;
import com.quedate.repository.PublicationRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.repository.UniversityRepository;
import com.quedate.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
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
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private LandlordRepository landlordRepository;
    @Mock
    private UniversityRepository universityRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private PublicationRepository publicationRepository;

    @InjectMocks
    private RoomService roomService;

    private UserPrincipal landlordPrincipal;
    private UserPrincipal userPrincipal;
    private Landlord landlord;

    @BeforeEach
    void setUp() {
        landlordPrincipal = principal(2L, RoleName.LANDLORD);
        userPrincipal = principal(3L, RoleName.USER);
        landlord = new Landlord();
        landlord.setId(1L);
        User ownerUser = new User();
        ownerUser.setId(2L);
        landlord.setUser(ownerUser);
    }

    @Test
    void create_persistsRoomOwnedByAuthenticatedLandlord() {
        RoomCreateDTO dto = new RoomCreateDTO();
        dto.setTitle("Cuarto A");
        dto.setPrice(new BigDecimal("500"));
        dto.setCapacity(1);

        when(landlordRepository.findByUserId(2L)).thenReturn(Optional.of(landlord));
        when(roomRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = roomService.create(landlordPrincipal, dto);

        assertEquals("Cuarto A", result.getTitle());
        assertEquals(new BigDecimal("500"), result.getPrice());
        assertEquals(RoomStatus.AVAILABLE, result.getStatus());
        verify(roomRepository).save(any());
    }

    @Test
    void create_rejectsNonLandlord() {
        RoomCreateDTO dto = new RoomCreateDTO();
        dto.setTitle("Cuarto X");
        dto.setPrice(new BigDecimal("300"));

        assertThrows(ForbiddenException.class,
                () -> roomService.create(userPrincipal, dto));
        verify(roomRepository, never()).save(any());
    }

    @Test
    void create_rejectsInvalidTitleAndPrice() {
        RoomCreateDTO dto = new RoomCreateDTO();
        dto.setTitle(" ");
        dto.setPrice(new BigDecimal("0"));

        assertThrows(InvalidOperationException.class,
                () -> roomService.create(landlordPrincipal, dto));
    }

    @Test
    void update_foreignRoomIsRejected() {
        Room room = room(landlord);
        RoomUpdateDTO dto = new RoomUpdateDTO();
        dto.setPrice(new BigDecimal("900"));

        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));

        assertThrows(ForbiddenException.class,
                () -> roomService.update(10L, userPrincipal, dto));
    }

    @Test
    void update_adminCanUpdateAnyRoom() {
        Room room = room(landlord);
        RoomUpdateDTO dto = new RoomUpdateDTO();
        dto.setPrice(new BigDecimal("900"));

        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));

        var result = roomService.update(10L, principal(1L, RoleName.ADMIN), dto);

        assertEquals(new BigDecimal("900"), result.getPrice());
    }

    @Test
    void delete_removesPublicationThenRoom() {
        Room room = room(landlord);
        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));

        roomService.delete(10L, landlordPrincipal);

        verify(publicationRepository).deleteByRoom_Id(10L);
        verify(roomRepository).delete(room);
    }

    private Room room(Landlord owner) {
        Room room = new Room();
        room.setId(10L);
        room.setTitle("Test Room");
        room.setPrice(new BigDecimal("500"));
        room.setStatus(RoomStatus.AVAILABLE);
        room.setImages(new ArrayList<>());
        room.setOwner(owner);
        return room;
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