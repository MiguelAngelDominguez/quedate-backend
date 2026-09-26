package com.quedate.service.room;

import com.quedate.dto.room.PublicationResponseDTO;
import com.quedate.entity.Publication;
import com.quedate.entity.PublicationStatus;
import com.quedate.entity.Room;
import com.quedate.exception.ForbiddenException;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.PublicationRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final RoomRepository roomRepository;

    public PublicationService(
            PublicationRepository publicationRepository,
            RoomRepository roomRepository
    ) {
        this.publicationRepository = publicationRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public PublicationResponseDTO publish(Long roomId, UserPrincipal principal) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomId));

        requireOwnerOrAdmin(room, principal);

        Publication publication = publicationRepository
                .findByRoom_Id(roomId)
                .orElseGet(Publication::new);

        publication.setRoom(room);
        publication.setStatus(PublicationStatus.ACTIVE);
        publication.setPublishedAt(LocalDateTime.now());
        publication.setArchivedAt(null);

        return toDTO(publicationRepository.save(publication));
    }

    @Transactional
    public PublicationResponseDTO archive(Long roomId, UserPrincipal principal) {
        Publication publication = publicationRepository
                .findByRoom_Id(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found: " + roomId));

        requireOwnerOrAdmin(publication.getRoom(), principal);

        publication.setStatus(PublicationStatus.ARCHIVED);
        publication.setArchivedAt(LocalDateTime.now());

        return toDTO(publicationRepository.save(publication));
    }

    private PublicationResponseDTO toDTO(Publication publication) {
        Room room = publication.getRoom();

        return PublicationResponseDTO.builder()
                .id(publication.getId())
                .roomId(room.getId())
                .roomTitle(room.getTitle())
                .status(publication.getStatus())
                .publishedAt(publication.getPublishedAt())
                .archivedAt(publication.getArchivedAt())
                .build();
    }

    private void requireOwnerOrAdmin(Room room, UserPrincipal principal) {
        boolean admin = principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        boolean owner = room.getOwner() != null
                && room.getOwner().getUser() != null
                && room.getOwner().getUser().getId().equals(principal.getUserId());

        if (!admin && !owner) {
            throw new ForbiddenException("You can only manage your own rooms");
        }
    }
}