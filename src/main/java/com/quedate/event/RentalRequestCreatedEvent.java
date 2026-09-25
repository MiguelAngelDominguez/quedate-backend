package com.quedate.event;

import com.quedate.entity.RentalRequest;

public class RentalRequestCreatedEvent {

    private final RentalRequest rentalRequest;

    public RentalRequestCreatedEvent(RentalRequest rentalRequest) {
        this.rentalRequest = rentalRequest;
    }

    public RentalRequest getRentalRequest() {
        return rentalRequest;
    }
}
