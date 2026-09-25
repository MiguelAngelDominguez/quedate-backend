package com.quedate.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDTO {

    @Min(1)
    @Max(5)
    private int rating;

    @Size(max = 500)
    private String comment;
}