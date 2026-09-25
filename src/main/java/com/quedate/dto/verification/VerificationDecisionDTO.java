package com.quedate.dto.verification;

import com.quedate.entity.enums.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDecisionDTO {

    @NotNull
    private VerificationStatus status;
}