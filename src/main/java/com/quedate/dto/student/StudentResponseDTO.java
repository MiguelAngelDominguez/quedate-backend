package com.quedate.dto.student;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {

    private Long id;
    private Long userId;
    private String dni;
    private String program;
    private Integer entryYear;
    private String scholarshipCode;
}