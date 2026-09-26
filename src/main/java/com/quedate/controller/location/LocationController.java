package com.quedate.controller.location;

import com.quedate.dto.location.LocationDTO;
import com.quedate.entity.Location;
import com.quedate.service.location.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationDTO> findAll(
            @RequestParam(required = false) String district
    ) {
        List<Location> locations;

        if (district != null && !district.isBlank()) {
            locations = locationService.findByDistrict(district);
        } else {
            locations = locationService.findAll();
        }

        return locations.stream()
                .map(this::toDTO)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO create(@RequestBody LocationDTO dto) {
        Location location = new Location();

        location.setDistrict(dto.getDistrict());
        location.setAddress(dto.getAddress());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setDistanceToUniversityKm(dto.getDistanceToUniversityKm());

        return toDTO(locationService.save(location));
    }

    private LocationDTO toDTO(Location location) {
        LocationDTO dto = new LocationDTO();

        dto.setId(location.getId());
        dto.setDistrict(location.getDistrict());
        dto.setAddress(location.getAddress());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setDistanceToUniversityKm(location.getDistanceToUniversityKm());

        return dto;
    }
}