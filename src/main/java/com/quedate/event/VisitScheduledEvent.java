package com.quedate.event;

import com.quedate.entity.Visit;

public class VisitScheduledEvent {

    private final Visit visit;

    public VisitScheduledEvent(Visit visit) {
        this.visit = visit;
    }

    public Visit getVisit() {
        return visit;
    }
}
