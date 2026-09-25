package com.quedate.dto.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

    private Long id;
    private String district;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double distanceToUniversityKm;
}
