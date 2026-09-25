package com.quedate.dto.request;

import com.quedate.entity.enums.RentalRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusUpdateDTO {

    @NotNull
    private RentalRequestStatus status;
}