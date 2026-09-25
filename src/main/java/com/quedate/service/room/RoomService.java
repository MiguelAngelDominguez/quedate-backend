package com.quedate.service.room;

import com.quedate.dto.room.RoomDetailDTO;
import com.quedate.dto.room.RoomSearchFilterDTO;
import com.quedate.dto.room.RoomSummaryDTO;
import com.quedate.entity.Room;
import com.quedate.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<RoomSummaryDTO> search(
            RoomSearchFilterDTO filter,
            Pageable pageable
    ) {
        Page<Room> rooms = roomRepository.search(
                filter.getUniversityId(),
                filter.getDistrict(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                pageable
        );

        return rooms.map(this::toSummaryDTO);
    }

    public RoomDetailDTO getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return toDetailDTO(room);
    }

    public Page<RoomSummaryDTO> getByLandlord(
            Long landlordId,
            Pageable pageable
    ) {
        return roomRepository.findByOwner_Id(landlordId, pageable)
                .map(this::toSummaryDTO);
    }

    private RoomSummaryDTO toSummaryDTO(Room room) {
        RoomSummaryDTO dto = new RoomSummaryDTO();

        dto.setId(room.getId());
        dto.setTitle(room.getTitle());
        dto.setPrice(room.getPrice());
        dto.setDistrict(
                room.getLocation() != null
                        ? room.getLocation().getDistrict()
                        : null
        );
        dto.setUniversityName(
                room.getUniversity() != null
                        ? room.getUniversity().getName()
                        : null
        );

        if (room.getImages() != null && !room.getImages().isEmpty()) {
            dto.setImage(room.getImages().get(0));
        }

        return dto;
    }

    private RoomDetailDTO toDetailDTO(Room room) {
        RoomDetailDTO dto = new RoomDetailDTO();

        dto.setId(room.getId());
        dto.setTitle(room.getTitle());
        dto.setDescription(room.getDescription());
        dto.setPrice(room.getPrice());
        dto.setCapacity(room.getCapacity());
        dto.setSizeM2(room.getSizeM2());
        dto.setImages(room.getImages());
        dto.setStatus(room.getStatus());
        dto.setVerified(room.isVerified());

        dto.setDistrict(
                room.getLocation() != null
                        ? room.getLocation().getDistrict()
                        : null
        );

        dto.setUniversityName(
                room.getUniversity() != null
                        ? room.getUniversity().getName()
                        : null
        );

        return dto;
    }
}
