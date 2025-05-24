package com.ticketnow.account.rest;

import com.ticketnow.account.dto.FeedbackCreateDTO;
import com.ticketnow.account.dto.FeedbackDTO;
import com.ticketnow.account.dto.StatusDTO;
import com.ticketnow.account.rest.response.APIResponse;
import com.ticketnow.account.rest.response.APIResponseBuilder;
import com.ticketnow.account.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/feedback")
@RestController
public class FeedbackResource {
    private final FeedbackService feedbackService;

    public FeedbackResource(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("")
    public APIResponse<List<FeedbackDTO>> get() {
        APIResponseBuilder<List<FeedbackDTO>> builder = new APIResponseBuilder<>();
        List<FeedbackDTO> feedbacks = feedbackService.getAllByUser();
        return builder.data(feedbacks).ok().build();
    }

    @GetMapping("/all")
    public APIResponse<List<FeedbackDTO>> getAll() {
        APIResponseBuilder<List<FeedbackDTO>> builder = new APIResponseBuilder<>();
        List<FeedbackDTO> feedbacks = feedbackService.getAll();
        return builder.data(feedbacks).ok().build();
    }

    @GetMapping("/{id}")
    public APIResponse<FeedbackDTO> getOne(@PathVariable Long id) {
        APIResponseBuilder<FeedbackDTO> builder = new APIResponseBuilder<>();
        FeedbackDTO feedback = feedbackService.getOne(id);
        return builder.data(feedback).ok().build();
    }

    @PostMapping("")
    public APIResponse<Long> create(@RequestBody FeedbackCreateDTO feedbackDTO) {
        APIResponseBuilder<Long> builder = new APIResponseBuilder<>();
        Long id = feedbackService.createFeedback(feedbackDTO);
        return builder.data(id).ok().build();
    }

    @PutMapping("/{id}")
    public APIResponse<Long> update(@PathVariable Long id, @RequestBody FeedbackDTO feedbackDTO) {
        APIResponseBuilder<Long> builder = new APIResponseBuilder<>();
        Long updatedId = feedbackService.updateFeedback(id, feedbackDTO);
        return builder.data(updatedId).ok().build();
    }

    @PutMapping("/{id}/status")
    public APIResponse<Long> resolve(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        APIResponseBuilder<Long> builder = new APIResponseBuilder<>();
        Long updatedId = feedbackService.resolveFeedback(id, statusDTO);
        return builder.data(updatedId).ok().build();
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable Long id) {
        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
        feedbackService.deleteFeedback(id);
        return builder.ok().build();
    }
}
