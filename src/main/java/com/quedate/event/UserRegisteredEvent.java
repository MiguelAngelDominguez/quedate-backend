package com.quedate.event;

import com.quedate.entity.User;

public class UserRegisteredEvent {

    private final User user;

    public UserRegisteredEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}