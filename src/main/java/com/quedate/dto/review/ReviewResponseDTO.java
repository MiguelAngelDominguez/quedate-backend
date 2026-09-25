package com.quedate.dto.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {

    private Long id;
    private int rating;
    private String comment;
    private Long studentId;
    private Long roomId;
    private LocalDateTime createdAt;
}