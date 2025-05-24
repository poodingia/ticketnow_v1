import axios from "axios";
import {OrderDetail, PaymentStatusData, OrderItem, OrderDashboardData} from "@/model/order.model";
import { API_ORDER_URL } from "@/utils/constant";

const api = axios.create({
    baseURL: API_ORDER_URL,
    headers: { "Content-Type": "application/json" }
});

export const createOrder = async (orderData: OrderDetail) => {
    const { data } = await api.post("", orderData);
    return data;
};

export const updatePaymentStatus = (paymentStatusData: PaymentStatusData) => {
    if (paymentStatusData.status === "CANCELLED") {
        api.post(`/payment/01/${paymentStatusData.ref}`).then();
    } else {
        api.post(`/payment/${paymentStatusData.code}/${paymentStatusData.ref}`).then();
    }
};

export const fetchOrders = async (): Promise<OrderDetail[]> => {
    const { data } = await api.get("");
    return data.data;
};

export const fetchOrdersByOrganizers = async (id : string): Promise<OrderDetail[]> => {
    const { data } = await api.get(`/organizer?eventId=${id}`);
    return data.data;
};

export const fetchOrderItems = async (orderId: number): Promise<OrderItem[]> => {
    const { data } = await api.get(`/${orderId}/item`);
    return data.data;
};

export const getOrderDashboardData = async (): Promise<OrderDashboardData> => {
    const { data } = await api.get("/statistic");
    return data.data;
}

export const getOrderDashboardDataByOrganizers = async (id : string): Promise<OrderDashboardData> => {
    const { data } = await api.get(`/statistic/${id}/organizer`);
    return data.data;
}