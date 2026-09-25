package com.quedate.dto.landlord;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LandlordUpdateDTO {

    @Pattern(regexp = "\\d{8}", message = "DNI must have 8 digits")
    private String dni;

    @Size(max = 1000)
    private String bio;
}