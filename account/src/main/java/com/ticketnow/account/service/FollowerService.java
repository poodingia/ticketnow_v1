package com.ticketnow.account.service;

import com.ticketnow.account.domain.UserEventFollow;
import com.ticketnow.account.domain.UserEventFollowId;
import com.ticketnow.account.repos.UserEventFollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowerService {
    private final UserEventFollowRepository userEventFollowRepository;

    public FollowerService(UserEventFollowRepository userEventFollowRepository) {
        this.userEventFollowRepository = userEventFollowRepository;
    }

    public void followEvent(String userId, Integer eventId) {
        UserEventFollow userEventFollow = new UserEventFollow(userId, eventId);
        userEventFollowRepository.save(userEventFollow);
    }

    public void unfollowEvent(String userId, Integer eventId) {
        userEventFollowRepository.deleteById(new UserEventFollowId(userId, eventId));
    }

    public Boolean isFollowingEvent(String userId, Integer eventId) {
        return userEventFollowRepository.existsById(new UserEventFollowId(userId, eventId));
    }
}
