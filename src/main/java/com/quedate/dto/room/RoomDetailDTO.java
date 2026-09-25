package com.quedate.dto.room;

import com.quedate.entity.RoomStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomDetailDTO {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer capacity;
    private Double sizeM2;
    private List<String> images;
    private RoomStatus status;
    private String ownerName;
    private String district;
    private String universityName;
    private Double averageRating;
    private boolean isVerified;
    private Double distanceKm;
}
