package com.quedate.controller.room;

import com.quedate.dto.room.RoomDetailDTO;
import com.quedate.dto.room.RoomSearchFilterDTO;
import com.quedate.dto.room.RoomSummaryDTO;
import com.quedate.service.room.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public Page<RoomSummaryDTO> search(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) Long universityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "price") String sortBy
    ) {
        RoomSearchFilterDTO filter = new RoomSearchFilterDTO();

        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setDistrict(district);
        filter.setUniversityId(universityId);
        filter.setPage(page);
        filter.setSize(size);
        filter.setSortBy(sortBy);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("price")
        );

        return roomService.search(filter, pageable);
    }

    @GetMapping("/{id}")
    public RoomDetailDTO getById(@PathVariable Long id) {
        return roomService.getById(id);
    }

    @GetMapping("/my-rooms")
    public Page<RoomSummaryDTO> getMyRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return Page.empty();
    }
}