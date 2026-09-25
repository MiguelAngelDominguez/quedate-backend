package com.quedate.dto.visit;

import com.quedate.entity.enums.VisitStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitStatusUpdateDTO {

    @NotNull
    private VisitStatus status;
}