package com.quedate.controller.landlord;

import com.quedate.dto.room.RoomSummaryDTO;
import com.quedate.service.room.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/landlords")
public class LandlordRoomController {

    private final RoomService roomService;

    public LandlordRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/{id}/rooms")
    public Page<RoomSummaryDTO> getByLandlord(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return roomService.getByLandlord(id, pageable);
    }
}
