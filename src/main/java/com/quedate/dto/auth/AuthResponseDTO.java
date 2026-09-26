package com.quedate.dto.auth;

import com.quedate.dto.user.UserResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

    private String accessToken;
    private String refreshToken;
    private UserResponseDTO userInfo;
}