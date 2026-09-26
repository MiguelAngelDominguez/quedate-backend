package com.quedate.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestCreateDTO {

    private String message;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @AssertTrue(message = "End date must be greater than or equal to start date")
    public boolean isDateRangeValid() {
        return startDate != null
                && endDate != null
                && !endDate.isBefore(startDate);
    }
}