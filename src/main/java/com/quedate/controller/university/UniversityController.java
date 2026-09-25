package com.quedate.controller.university;

import com.quedate.dto.university.UniversityCreateDTO;
import com.quedate.dto.university.UniversityDTO;
import com.quedate.entity.University;
import com.quedate.service.university.UniversityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/universities")
public class UniversityController {

    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public List<UniversityDTO> findAll() {
        return universityService.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public UniversityDTO findById(@PathVariable Long id) {
        return toDTO(universityService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UniversityDTO create(
            @Valid @RequestBody UniversityCreateDTO dto
    ) {
        University university = new University();
        university.setName(dto.getName());
        university.setCampusName(dto.getCampusName());
        university.setAddress(dto.getAddress());
        university.setLatitude(dto.getLatitude());
        university.setLongitude(dto.getLongitude());
        university.setWebsite(dto.getWebsite());

        return toDTO(universityService.save(university));
    }

    private UniversityDTO toDTO(University university) {
        UniversityDTO dto = new UniversityDTO();
        dto.setId(university.getId());
        dto.setName(university.getName());
        dto.setCampusName(university.getCampusName());
        dto.setAddress(university.getAddress());
        dto.setLatitude(university.getLatitude());
        dto.setLongitude(university.getLongitude());
        dto.setWebsite(university.getWebsite());
        return dto;
    }
}
