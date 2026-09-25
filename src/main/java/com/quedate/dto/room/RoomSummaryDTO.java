package com.quedate.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RoomSummaryDTO {

    private Long id;
    private String title;
    private BigDecimal price;
    private String image;
    private String district;
    private String universityName;
    private Double distanceKm;
}
