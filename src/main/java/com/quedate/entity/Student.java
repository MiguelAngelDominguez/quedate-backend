package com.quedate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 digitos")
    @Column(unique = true)
    private String dni;

    private String program;

    private Integer entryYear;

    private String scholarshipCode;
}