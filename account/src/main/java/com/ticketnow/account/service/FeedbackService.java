package com.ticketnow.account.service;

import com.ticketnow.account.config.KeycloakPropsConfig;
import com.ticketnow.account.domain.Feedback;
import com.ticketnow.account.domain.FeedbackStatus;
import com.ticketnow.account.dto.FeedbackCreateDTO;
import com.ticketnow.account.dto.FeedbackDTO;
import com.ticketnow.account.dto.StatusDTO;
import com.ticketnow.account.repos.FeedbackRepository;
import com.ticketnow.account.utils.AuthenticationUtils;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final Keycloak keycloak;
    private final KeycloakPropsConfig keycloakPropsConfig;

    public FeedbackService(FeedbackRepository feedbackRepository, Keycloak keycloak, KeycloakPropsConfig keycloakPropsConfig) {
        this.feedbackRepository = feedbackRepository;
        this.keycloak = keycloak;
        this.keycloakPropsConfig = keycloakPropsConfig;
    }

    public FeedbackDTO getOne(Long id) {
        return feedbackRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    public List<FeedbackDTO> getAllByUser() {
        return feedbackRepository.findAllByUserId(AuthenticationUtils.extractUserId()).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<FeedbackDTO> getAll() {
        return feedbackRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Long updateFeedback(Long id, FeedbackDTO feedbackDTO) {
        return feedbackRepository.findById(id).map(feedback -> {
            mapToEntity(feedbackDTO, feedback);
            return feedbackRepository.saveAndFlush(feedback).getId();
        }).orElse(null);
    }

    public Long resolveFeedback(Long id, StatusDTO dto) {
        return feedbackRepository.findById(id).map(feedback -> {
            feedback.setStatus(FeedbackStatus.valueOf(dto.status()));
            return feedbackRepository.saveAndFlush(feedback).getId();
        }).orElse(null);
    }

    public Long createFeedback(FeedbackCreateDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setUserId(AuthenticationUtils.extractUserId());
        feedback.setTopic(feedbackDTO.topic());
        feedback.setMessage(feedbackDTO.message());
        feedback.setStatus(FeedbackStatus.PENDING);
        return feedbackRepository.saveAndFlush(feedback).getId();
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public void mapToEntity(FeedbackDTO feedbackDTO, final Feedback feedback) {
        feedback.setUserId(feedbackDTO.userId());
        feedback.setTopic(feedbackDTO.topic());
        feedback.setMessage(feedbackDTO.message());
    }

    public FeedbackDTO mapToDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getCreatedAt(),
                feedback.getUpdatedAt(),
                feedback.getId(),
                feedback.getUserId(),
                feedback.getTopic(),
                feedback.getMessage(),
                feedback.getStatus().name(),
                keycloak.realm(keycloakPropsConfig.getRealm()).users().get(feedback.getUserId()).toRepresentation().getUsername()
        );
    }


}
