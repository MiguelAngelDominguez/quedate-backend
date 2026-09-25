package com.quedate.service.room;

import com.quedate.entity.Room;
import com.quedate.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<Room> findAll(
            Long universityId,
            String district,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        return roomRepository.search(
                universityId,
                district,
                minPrice,
                maxPrice,
                pageable
        );
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Page<Room> findByOwner(Long ownerId, Pageable pageable) {
        return roomRepository.findByOwner_Id(ownerId, pageable);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public Room update(Long id, Room room) {
        Room existingRoom = findById(id);

        existingRoom.setTitle(room.getTitle());
        existingRoom.setDescription(room.getDescription());
        existingRoom.setPrice(room.getPrice());
        existingRoom.setCapacity(room.getCapacity());
        existingRoom.setSizeM2(room.getSizeM2());
        existingRoom.setImages(room.getImages());
        existingRoom.setStatus(room.getStatus());
        existingRoom.setUniversity(room.getUniversity());
        existingRoom.setLocation(room.getLocation());

        return roomRepository.save(existingRoom);
    }

    public void delete(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }
}
