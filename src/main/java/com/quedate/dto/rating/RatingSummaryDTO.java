package com.quedate.dto.rating;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingSummaryDTO {

    private Double average;
    private Long count;
}