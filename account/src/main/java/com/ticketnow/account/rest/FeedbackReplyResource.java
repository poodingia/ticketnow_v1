package com.ticketnow.account.rest;

import com.ticketnow.account.dto.FeedbackReplyCreateDTO;
import com.ticketnow.account.dto.FeedbackReplyDTO;
import com.ticketnow.account.rest.response.APIResponse;
import com.ticketnow.account.rest.response.APIResponseBuilder;
import com.ticketnow.account.service.FeedbackReplyService;
import com.ticketnow.account.utils.AuthenticationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/feedback/{id}/feedback-replies")
@RestController
public class FeedbackReplyResource {
    private final FeedbackReplyService feedbackReplyService;

    public FeedbackReplyResource(FeedbackReplyService feedbackReplyService) {
        this.feedbackReplyService = feedbackReplyService;
    }

    @GetMapping("")
    public APIResponse<List<FeedbackReplyDTO>> get(@PathVariable Long id) {
        APIResponseBuilder<List<FeedbackReplyDTO>> builder = new APIResponseBuilder<>();
        List<FeedbackReplyDTO> feedbackReplies = feedbackReplyService.getFeedbackReplies(id);
        return builder.data(feedbackReplies).ok().build();
    }

    @GetMapping("/{replyId}")
    public APIResponse<FeedbackReplyDTO> getOne(@PathVariable Long id, @PathVariable Long replyId) {
        APIResponseBuilder<FeedbackReplyDTO> builder = new APIResponseBuilder<>();
        FeedbackReplyDTO feedbackReply = feedbackReplyService.getFeedbackReply(replyId);
        return builder.data(feedbackReply).ok().build();
    }

//    @PutMapping("/{replyId}")
//    public APIResponse<Long> update(@PathVariable Long replyId, @RequestBody FeedbackReplyCreateDTO feedbackReplyDTO) {
//        APIResponseBuilder<Long> builder = new APIResponseBuilder<>();
//        Long updatedId = feedbackReplyService.updateFeedbackReply(replyId, feedbackReplyDTO.message());
//        return builder.data(updatedId).ok().build();
//    }

    @PostMapping("")
    public APIResponse<Long> create(@PathVariable long id ,@RequestBody FeedbackReplyCreateDTO feedbackReplyDTO) {
        APIResponseBuilder<Long> builder = new APIResponseBuilder<>();
        Long createdId = feedbackReplyService.createFeedbackReply(AuthenticationUtils.extractUserId(), feedbackReplyDTO.message(), id);
        return builder.data(createdId).ok().build();
    }

//    @DeleteMapping("/{replyId}")
//    public APIResponse<Void> delete(@PathVariable Long replyId) {
//        APIResponseBuilder<Void> builder = new APIResponseBuilder<>();
//        feedbackReplyService.deleteFeedbackReply(replyId);
//        return builder.ok().build();
//    }

}
