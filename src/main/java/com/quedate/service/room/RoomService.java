package com.quedate.service.room;

import com.quedate.dto.room.RoomCreateDTO;
import com.quedate.dto.room.RoomDetailDTO;
import com.quedate.dto.room.RoomSearchFilterDTO;
import com.quedate.dto.room.RoomSummaryDTO;
import com.quedate.dto.room.RoomUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.Location;
import com.quedate.entity.Room;
import com.quedate.entity.RoomStatus;
import com.quedate.entity.University;
import com.quedate.exception.ForbiddenException;
import com.quedate.exception.InvalidOperationException;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.LocationRepository;
import com.quedate.repository.PublicationRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.repository.UniversityRepository;
import com.quedate.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final LandlordRepository landlordRepository;
    private final UniversityRepository universityRepository;
    private final LocationRepository locationRepository;
    private final PublicationRepository publicationRepository;

    public RoomService(
            RoomRepository roomRepository,
            LandlordRepository landlordRepository,
            UniversityRepository universityRepository,
            LocationRepository locationRepository,
            PublicationRepository publicationRepository
    ) {
        this.roomRepository = roomRepository;
        this.landlordRepository = landlordRepository;
        this.universityRepository = universityRepository;
        this.locationRepository = locationRepository;
        this.publicationRepository = publicationRepository;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public RoomDetailDTO getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return toDetailDTO(room);
    }

    @Transactional(readOnly = true)
    public Page<RoomSummaryDTO> getByLandlord(
            Long landlordId,
            Pageable pageable
    ) {
        return roomRepository.findByOwner_Id(landlordId, pageable)
                .map(this::toSummaryDTO);
    }

    @Transactional
    public RoomDetailDTO create(UserPrincipal principal, RoomCreateDTO dto) {
        requireLandlord(principal);

        String title = dto.getTitle();
        BigDecimal price = dto.getPrice();
        if (title == null || title.isBlank() || price == null || price.signum() <= 0) {
            throw new InvalidOperationException("Title and a positive price are required");
        }

        Landlord owner = landlordRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Landlord profile not found"));

        Room room = new Room();
        room.setTitle(title);
        room.setDescription(dto.getDescription());
        room.setPrice(price);
        room.setCapacity(dto.getCapacity());
        room.setSizeM2(dto.getSizeM2());
        if (dto.getImages() != null) {
            room.setImages(dto.getImages());
        }
        room.setStatus(RoomStatus.AVAILABLE);
        room.setOwner(owner);
        room.setUniversity(resolveUniversity(dto.getUniversityId()));
        room.setLocation(resolveLocation(dto.getLocationId()));

        return toDetailDTO(roomRepository.save(room));
    }

    @Transactional
    public RoomDetailDTO update(Long id, UserPrincipal principal, RoomUpdateDTO dto) {
        Room room = loadOwnedRoom(id, principal);

        if (dto.getTitle() != null) {
            room.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            room.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            room.setPrice(dto.getPrice());
        }
        if (dto.getCapacity() != null) {
            room.setCapacity(dto.getCapacity());
        }
        if (dto.getSizeM2() != null) {
            room.setSizeM2(dto.getSizeM2());
        }
        if (dto.getImages() != null) {
            room.setImages(dto.getImages());
        }
        if (dto.getStatus() != null) {
            room.setStatus(dto.getStatus());
        }
        if (dto.getUniversityId() != null) {
            room.setUniversity(resolveUniversity(dto.getUniversityId()));
        }
        if (dto.getLocationId() != null) {
            room.setLocation(resolveLocation(dto.getLocationId()));
        }

        return toDetailDTO(room);
    }

    @Transactional
    public void delete(Long id, UserPrincipal principal) {
        Room room = loadOwnedRoom(id, principal);
        publicationRepository.deleteByRoom_Id(id);
        roomRepository.delete(room);
    }

    @Transactional(readOnly = true)
    public Page<RoomSummaryDTO> getByCurrentLandlord(
            UserPrincipal principal,
            Pageable pageable
    ) {
        requireLandlord(principal);

        Landlord landlord = landlordRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new ForbiddenException("Landlord profile not found"));

        return getByLandlord(landlord.getId(), pageable);
    }

    private Room loadOwnedRoom(Long id, UserPrincipal principal) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));

        if (!isOwnerOrAdmin(room, principal)) {
            throw new ForbiddenException("You can only manage your own rooms");
        }

        return room;
    }

    private void requireLandlord(UserPrincipal principal) {
        boolean landlord = principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_LANDLORD".equals(a.getAuthority()));

        if (!landlord) {
            throw new ForbiddenException("Only landlords can manage rooms");
        }
    }

    private boolean isOwnerOrAdmin(Room room, UserPrincipal principal) {
        boolean admin = principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        boolean owner = room.getOwner() != null
                && room.getOwner().getUser() != null
                && room.getOwner().getUser().getId().equals(principal.getUserId());

        return admin || owner;
    }

    private University resolveUniversity(Long universityId) {
        if (universityId == null) {
            return null;
        }

        return universityRepository.findById(universityId)
                .orElseThrow(() -> new ResourceNotFoundException("University not found: " + universityId));
    }

    private Location resolveLocation(Long locationId) {
        if (locationId == null) {
            return null;
        }

        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found: " + locationId));
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
        dto.setImages(new ArrayList<>(room.getImages()));
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
