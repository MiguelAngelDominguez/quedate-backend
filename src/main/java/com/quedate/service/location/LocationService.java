package com.quedate.service.location;

import com.quedate.entity.Location;
import com.quedate.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public List<Location> findByDistrict(String district) {
        return locationRepository.findByDistrictIgnoreCase(district);
    }

    public Location save(Location location) {
        return locationRepository.save(location);
    }
}
