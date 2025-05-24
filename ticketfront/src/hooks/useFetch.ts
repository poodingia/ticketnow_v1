import {useQuery} from "@tanstack/react-query";
import {
  fetchCouponDetailById,
  fetchCouponsById, fetchEventDetail,
  fetchEventsByOrganizer,
  fetchOrganizerStats,
  fetchQueue
} from "@/api/event";
import {fetchOrderItems, fetchOrders, fetchOrdersByOrganizers, getOrderDashboardData, getOrderDashboardDataByOrganizers} from "@/api/order";
import {getAuthenticationInfo} from "@/api/auth";
import {fetchTicketTypes} from "@/api/ticketType";
import {checkFollowEvent, getCustomerProfile} from "@/api/account";
import {getFeedbackDetail, getFeedbackReplies, getFeedbacks} from "@/api/feedback";
import { searchSimilarEvents } from "@/api/search";
import {getALlCities} from "@/api/city";
import {QUEUE_REFETCH_INTERVAL} from "@/utils/constant";

export const useCities = () => useQuery({
  queryKey: ["cities"],
  queryFn: () => getALlCities(),
})

export const useOrganizerEvents = () => useQuery({
  queryKey: ["events"],
  queryFn: () => fetchEventsByOrganizer(),
});

export const useEventDetail = (id: string) => useQuery({
  queryKey: ["event", id],
  queryFn: () => fetchEventDetail(id),
  enabled: !!id,
})

export const useOrders = () => useQuery({
  queryKey: ["orders"],
  queryFn: () => fetchOrders(),
})

export const useOrdersOrganizer = (id : string) => useQuery({
  queryKey: ["orders", id],
  queryFn: () => fetchOrdersByOrganizers(id),
})

export const useAuthenticate = () => useQuery({
  queryKey: ["authenticate"],
  queryFn: () => getAuthenticationInfo(),
})

export const useTicketTypes = (id: string) => useQuery({
  queryKey: ["ticketTypes", id],
  queryFn: () => fetchTicketTypes(id),
  enabled: !!id,
});

export const useOrderItems = (orderId: number, open: boolean) => useQuery({
  queryKey: ["orderItems", orderId],
  queryFn: () => fetchOrderItems(orderId),
  enabled: open,
})

export const useProfile = () => useQuery({
  queryKey: ["customer"],
  queryFn: () => getCustomerProfile(),
})

export const useFeedbackReplies = (id: string)  => useQuery({
  queryKey: ["feedbackReplies", id],
  queryFn: () => getFeedbackReplies(id),
});

export const useFeedbacks = () => useQuery({
  queryKey: ["feedbacks"],
  queryFn: () => getFeedbacks(),
})

export const useFeedbackDetail = (id: string) => useQuery({
  queryKey: ["feedback", id],
  queryFn: () => getFeedbackDetail(id),
});

export const useCouponDetailById = (id: number) => useQuery({
  queryKey: ["couponDetailById", id],
  queryFn: () => fetchCouponDetailById(id),
  enabled: id != 0,
})

export const useQueueSchedulePosition = (id: string) => useQuery<number>({
  queryKey: ["queuePosition", id],
  queryFn: () => fetchQueue(id),
  refetchInterval: QUEUE_REFETCH_INTERVAL,
  refetchOnMount: true,
  staleTime: 0,
})

export const useCoupons = (id: string) => useQuery({
  queryKey: ["coupons", id],
  queryFn: () => fetchCouponsById(id),
})

export const useFollowEvent = (id: string) => useQuery<boolean>({
  queryKey: ["followEvent", id],
  queryFn: () => checkFollowEvent(id),
})

export const useOrganizeStats = () => useQuery({
  queryKey: ["organizerStats"],
  queryFn: () => fetchOrganizerStats(),
})

export const useOrderStats = () => useQuery({
  queryKey: ["orderStats"],
  queryFn: () => getOrderDashboardData(),
})

export const useOrderStatsOrganizer = (id : string) => useQuery({
  queryKey: ["orderOrganizerStats", id],
  queryFn: () => getOrderDashboardDataByOrganizers(id),
})

export const useSimilarEvents = (title: string) => useQuery({
  queryKey: ["similarEvents", title],
  queryFn: () => searchSimilarEvents(title),
})