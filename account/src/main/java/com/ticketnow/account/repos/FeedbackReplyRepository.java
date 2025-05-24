package com.ticketnow.account.repos;

import com.ticketnow.account.domain.FeedbackReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackReplyRepository extends JpaRepository<FeedbackReply, Long> {
    List<FeedbackReply> findAllByFeedbackIdOrderByCreatedAtDesc(Long feedbackId);
}