package com.ticketnow.account.service;

import com.ticketnow.account.config.KeycloakPropsConfig;
import com.ticketnow.account.domain.Feedback;
import com.ticketnow.account.domain.FeedbackReply;
import com.ticketnow.account.domain.FeedbackStatus;
import com.ticketnow.account.dto.FeedbackReplyDTO;
import com.ticketnow.account.repos.FeedbackReplyRepository;
import com.ticketnow.account.repos.FeedbackRepository;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackReplyService {
    private final FeedbackReplyRepository feedbackReplyRepository;
    private final FeedbackRepository feedbackRepository;
    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;



    public FeedbackReplyService(FeedbackReplyRepository feedbackReplyRepository, FeedbackRepository feedbackRepository, Keycloak keycloak, KeycloakPropsConfig keycloakPropsConfig) {
        this.feedbackReplyRepository = feedbackReplyRepository;
        this.feedbackRepository = feedbackRepository;
        this.keycloak = keycloak;
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    public void deleteFeedbackReply(Long id) {
        feedbackReplyRepository.deleteById(id);
    }

    public Long updateFeedbackReply(Long id, String message) {
        feedbackReplyRepository.findById(id).ifPresent(feedbackReply -> {
            feedbackReply.setMessage(message);
            feedbackReplyRepository.saveAndFlush(feedbackReply);
        });
        return id;
    }

    public Long createFeedbackReply(String userId, String message, Long feedbackId) {
        FeedbackReply feedbackReply = new FeedbackReply();
        feedbackReply.setUserId(userId);
        feedbackReply.setMessage(message);
        feedbackReply.setFeedbackId(feedbackId);
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow();
        feedback.setStatus(FeedbackStatus.PENDING);
        feedbackRepository.saveAndFlush(feedback);
        return feedbackReplyRepository.saveAndFlush(feedbackReply).getId();
    }

    public FeedbackReplyDTO getFeedbackReply(Long id) {
        return feedbackReplyRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    public List<FeedbackReplyDTO> getFeedbackReplies(long feedbackId) {
        return feedbackReplyRepository.findAllByFeedbackIdOrderByCreatedAtDesc(feedbackId).stream().map(this::mapToDTO).toList();
    }

    public FeedbackReplyDTO mapToDTO(FeedbackReply feedbackReply) {
        return new FeedbackReplyDTO(feedbackReply.getCreatedAt(), feedbackReply.getUpdatedAt(), feedbackReply.getId(),
                feedbackReply.getUserId(), feedbackReply.getMessage(), feedbackReply.getFeedbackId(),
                keycloak.realm(keycloakPropsConfig.getRealm()).users().get(feedbackReply.getUserId()).toRepresentation().getUsername()
        );
    }
}
