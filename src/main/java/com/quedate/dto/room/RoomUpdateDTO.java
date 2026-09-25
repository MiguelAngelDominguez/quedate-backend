package com.quedate.dto.room;

import com.quedate.entity.RoomStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomUpdateDTO {

    @Size(max = 100)
    private String title;

    private String description;

    @Min(1)
    private BigDecimal price;

    @Min(1)
    private Integer capacity;

    private Double sizeM2;

    private List<String> images;

    private Long locationId;

    private Long universityId;

    private RoomStatus status;
}
