import {useQuery} from "@tanstack/react-query";
import {fetchEventDetail, fetchEvents, fetchAdminStats} from "@/api/event";
import {fetchTicketTypes} from "@/api/ticketType";
import {TicketType} from "@/model/ticketType.model";
import {Feedback, FeedbackReply} from "@/model/feedback.model";
import {getAllUserProfiles, getFeedbackDetail, getFeedbackReplies, getFeedbacks, getUserProfile} from "@/api/feedback";
import { Statistic } from "@/model/event.model";

export const useEvents = () => useQuery({
  queryKey: ["event"],
  queryFn: () => fetchEvents(''),
  refetchOnMount: 'always'
});

export const useEventDetail = (id: string) => useQuery({
  queryKey: ["event", id],
  queryFn: () => fetchEventDetail(id),
  enabled: !!id,
  refetchOnMount: 'always'
});

export const useTicketTypes = (id: string) => useQuery<TicketType[]>({
  queryKey: ["ticketTypes", id],
  queryFn: () => fetchTicketTypes(id),
  enabled: !!id,
});

export const useFeedbacks = () => useQuery<Feedback[]>({
  queryKey: ["feedbacks"],
  queryFn: () => getFeedbacks(),
  refetchOnMount: 'always',
});

export const useFeedbackDetail = (id: string) => useQuery<Feedback>({
  queryKey: ["feedback", id],
  queryFn: () => getFeedbackDetail(id),
});

export const useFeedbackReplies = (id: string)  => useQuery<FeedbackReply[]>({
  queryKey: ["feedbackReplies", id],
  queryFn: () => getFeedbackReplies(id),
});

export const useAdminStats = () => useQuery<Statistic>({
  queryKey: ["adminStats"],
  queryFn: () => fetchAdminStats(),
})

export const useProfile = (email: string) => useQuery({
  queryKey: ["profile", email],
  queryFn: () => getUserProfile(email),
  enabled: !!email,
})

export const useAllUserProfiles = () => useQuery({
  queryKey: ["allUserProfiles"],
  queryFn: () => getAllUserProfiles(),
})


