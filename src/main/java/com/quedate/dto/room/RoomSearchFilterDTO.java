package com.quedate.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RoomSearchFilterDTO {

    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String district;
    private Long universityId;
    private Integer page;
    private Integer size;
    private String sortBy;
}
