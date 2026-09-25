package com.quedate.dto.student;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO {

    @Pattern(regexp = "\\d{8}", message = "DNI must have 8 digits")
    private String dni;

    @Size(max = 200)
    private String program;

    @Min(value = 2000, message = "entryYear must be >= 2000")
    private Integer entryYear;

    @Size(max = 100)
    private String scholarshipCode;
}