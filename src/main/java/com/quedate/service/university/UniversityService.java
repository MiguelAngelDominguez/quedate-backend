package com.quedate.service.university;

import com.quedate.entity.University;
import com.quedate.repository.UniversityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public University findById(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("University not found"));
    }

    public List<University> findByName(String name) {
        return universityRepository.findByNameContainingIgnoreCase(name);
    }

    public University save(University university) {
        return universityRepository.save(university);
    }
}
