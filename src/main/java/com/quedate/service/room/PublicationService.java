package com.quedate.service.room;

import com.quedate.entity.Publication;
import com.quedate.entity.PublicationStatus;
import com.quedate.entity.Room;
import com.quedate.repository.PublicationRepository;
import com.quedate.repository.RoomRepository;
import org.springframework.stereotype.Service;

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

    public Publication publish(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Publication publication = publicationRepository
                .findByRoom_Id(roomId)
                .orElseGet(Publication::new);

        publication.setRoom(room);
        publication.setStatus(PublicationStatus.ACTIVE);
        publication.setPublishedAt(LocalDateTime.now());
        publication.setArchivedAt(null);

        return publicationRepository.save(publication);
    }

    public Publication archive(Long roomId) {
        Publication publication = publicationRepository
                .findByRoom_Id(roomId)
                .orElseThrow(() -> new RuntimeException("Publication not found"));

        publication.setStatus(PublicationStatus.ARCHIVED);
        publication.setArchivedAt(LocalDateTime.now());

        return publicationRepository.save(publication);
    }
}
