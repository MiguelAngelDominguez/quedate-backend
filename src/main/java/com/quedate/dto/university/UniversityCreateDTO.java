package com.quedate.dto.university;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UniversityCreateDTO {

    @NotNull
    private String name;

    private String campusName;
    private String address;
    private Double latitude;
    private Double longitude;
    private String website;
}
