package com.ticketnow.account.repos;

import com.ticketnow.account.domain.UserEventFollow;
import com.ticketnow.account.domain.UserEventFollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserEventFollowRepository extends JpaRepository<UserEventFollow, UserEventFollowId> {
    @Query("SELECT u FROM UserEventFollow u WHERE u.id.eventId = :eventId")
    List<UserEventFollow> findAllUserIdsByEventId(Integer eventId);
}