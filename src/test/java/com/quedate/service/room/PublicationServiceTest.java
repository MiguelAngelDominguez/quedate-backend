package com.quedate.service.room;

import com.quedate.entity.Landlord;
import com.quedate.entity.Publication;
import com.quedate.entity.PublicationStatus;
import com.quedate.entity.Role;
import com.quedate.entity.Room;
import com.quedate.entity.RoomStatus;
import com.quedate.entity.User;
import com.quedate.entity.enums.RoleName;
import com.quedate.exception.ForbiddenException;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.PublicationRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
class PublicationServiceTest {

    @Mock
    private PublicationRepository publicationRepository;
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private PublicationService publicationService;

    private UserPrincipal ownerPrincipal;
    private UserPrincipal foreignPrincipal;

    @BeforeEach
    void setUp() {
        ownerPrincipal = principal(2L, RoleName.LANDLORD);
        foreignPrincipal = principal(9L, RoleName.LANDLORD);
    }

    @Test
    void publish_createsActivePublicationWhenOwner() {
        Room room = room(landlord(2L));
        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
        when(publicationRepository.findByRoom_Id(10L)).thenReturn(Optional.empty());
        when(publicationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = publicationService.publish(10L, ownerPrincipal);

        assertEquals(PublicationStatus.ACTIVE, result.getStatus());
        assertEquals(10L, result.getRoomId());
    }

    @Test
    void publish_rejectsForeignRoom() {
        Room room = room(landlord(2L));
        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));

        assertThrows(ForbiddenException.class,
                () -> publicationService.publish(10L, foreignPrincipal));
        verify(publicationRepository, never()).save(any());
    }

    @Test
    void archive_missingPublicationNotFound() {
        when(publicationRepository.findByRoom_Id(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> publicationService.archive(10L, ownerPrincipal));
    }

    @Test
    void archive_setsArchivedStatus() {
        Room room = room(landlord(2L));
        Publication publication = new Publication();
        publication.setId(5L);
        publication.setRoom(room);
        publication.setStatus(PublicationStatus.ACTIVE);

        when(publicationRepository.findByRoom_Id(10L)).thenReturn(Optional.of(publication));
        when(publicationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = publicationService.archive(10L, ownerPrincipal);

        assertEquals(PublicationStatus.ARCHIVED, result.getStatus());
    }

    private Landlord landlord(Long userId) {
        Landlord landlord = new Landlord();
        landlord.setId(1L);
        User user = new User();
        user.setId(userId);
        landlord.setUser(user);
        return landlord;
    }

    private Room room(Landlord owner) {
        Room room = new Room();
        room.setId(10L);
        room.setTitle("Test Room");
        room.setPrice(new BigDecimal("500"));
        room.setStatus(RoomStatus.AVAILABLE);
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