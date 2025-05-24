package com.ticketnow.account.rest;

import com.ticketnow.account.rest.response.APIResponse;
import com.ticketnow.account.rest.response.APIResponseBuilder;
import com.ticketnow.account.service.FollowerService;
import com.ticketnow.account.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
public class FollowerResource {
    private final FollowerService followerService;

    public FollowerResource(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PostMapping("/{id}")
    public APIResponse<Void> followEvent(@PathVariable(name = "id") Integer eventId) {
        String userId = AuthenticationUtils.extractUserId();
        APIResponseBuilder<Void> responseBuilder = new APIResponseBuilder<>();
        followerService.followEvent(userId, eventId);
        return responseBuilder.ok().build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> unfollowEvent(@PathVariable(name = "id") Integer eventId) {
        String userId = AuthenticationUtils.extractUserId();
        APIResponseBuilder<Void> responseBuilder = new APIResponseBuilder<>();
        followerService.unfollowEvent(userId, eventId);
        return responseBuilder.ok().build();
    }


    @GetMapping("/{id}")
    public APIResponse<Boolean> isFollowingEvent(@PathVariable(name = "id") Integer eventId) {
        String userId = AuthenticationUtils.extractUserId();
        APIResponseBuilder<Boolean> responseBuilder = new APIResponseBuilder<>();
        return responseBuilder.data(followerService.isFollowingEvent(userId, eventId)).ok().build();
    }
}
