package com.ticketnow.account.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users_events_follow")
public class UserEventFollow {

    @EmbeddedId
    private UserEventFollowId id;

    public UserEventFollow() {}

    public UserEventFollow(String userId, Integer eventId) {
        this.id = new UserEventFollowId(userId, eventId);
    }

    public String getUserId() {
        return id.getUserId();
    }

    public Integer getEventId() {
        return id.getEventId();
    }
}


