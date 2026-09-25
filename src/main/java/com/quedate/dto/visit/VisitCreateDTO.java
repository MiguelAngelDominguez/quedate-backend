package com.quedate.dto.visit;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitCreateDTO {

    @NotNull
    @Future
    private LocalDateTime scheduledAt;

    private String notes;
}