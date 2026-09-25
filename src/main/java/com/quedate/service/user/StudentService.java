package com.quedate.service.user;

import com.quedate.dto.student.StudentResponseDTO;
import com.quedate.dto.student.StudentUpdateDTO;
import com.quedate.entity.Student;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO getProfileByUserId(Long userId) {
        return buildResponse(findByUserId(userId));
    }

    @Transactional
    public StudentResponseDTO update(Long userId, StudentUpdateDTO dto) {
        Student student = findByUserId(userId);
        if (dto.getDni() != null) {
            student.setDni(dto.getDni());
        }
        if (dto.getProgram() != null) {
            student.setProgram(dto.getProgram());
        }
        if (dto.getEntryYear() != null) {
            student.setEntryYear(dto.getEntryYear());
        }
        if (dto.getScholarshipCode() != null) {
            student.setScholarshipCode(dto.getScholarshipCode());
        }
        return buildResponse(student);
    }

    private Student findByUserId(Long userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student profile not found for user: " + userId));
    }

    private StudentResponseDTO buildResponse(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .userId(student.getUser().getId())
                .dni(student.getDni())
                .program(student.getProgram())
                .entryYear(student.getEntryYear())
                .scholarshipCode(student.getScholarshipCode())
                .build();
    }
}