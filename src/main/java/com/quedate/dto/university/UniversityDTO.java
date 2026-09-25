package com.quedate.dto.university;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UniversityDTO {

    private Long id;
    private String name;
    private String campusName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String website;
}
