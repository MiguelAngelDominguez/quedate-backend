package com.quedate.dto.verification;

import com.quedate.entity.enums.VerificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestDTO {

    @NotNull
    private VerificationType type;

    @NotBlank
    private String documentReference;
}