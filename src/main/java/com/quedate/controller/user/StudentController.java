package com.quedate.controller.user;

import com.quedate.dto.student.StudentResponseDTO;
import com.quedate.service.user.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getByUserId(@PathVariable Long id) {
        return studentService.getProfileByUserId(id);
    }
}