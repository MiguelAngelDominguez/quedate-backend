package com.quedate.dto.landlord;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandlordResponseDTO {

    private Long id;
    private Long userId;
    private String dni;
    private String bio;
    private Boolean verified;
}