import { API_CRUD_URL, CLOUDINARY_URL } from "@/utils/constant";
import {
  Coupon,
  CreateCoupon,
  EventCreateGeneral,
  EventDetail,
  Statistic,
  EventCreateForm,
  EventSummary
} from "@/model/event.model";
import axios from "axios";

const api = axios.create({
  baseURL: API_CRUD_URL,
  headers: { "Content-Type": "application/json" }
});

export const fetchEventDetail = async (endpoint: string): Promise<EventDetail> => {
  const res = await fetch(`${API_CRUD_URL}/event/${endpoint}`, {next: { revalidate: 60 },});
  if (!res.ok) {
    throw new Error(`Failed to fetch events: ${res.statusText}`);
  }
  const data = await res.json();
  return data.data;
};

export const fetchEvents = async (): Promise<EventSummary[]> => {
  const res = await fetch(`${API_CRUD_URL}/event/approved`, {next: { revalidate: 60 },});
  if (!res.ok) {
    throw new Error(`Failed to fetch events: ${res.statusText}`);
  }
  const data = await res.json();
  return data.data;
};

export const fetchEventsByOrganizer = async (): Promise<EventDetail[]> => {
  const {data} = await api.get(`/organizer/events`);
  return data.data;
}

export const createEvent = async (formData: EventCreateForm) => {
  const imageUrl = await uploadImage(formData.event.image);
  const { data } = await api.post(`/event`, {
    ...formData,
    event: {
      ...formData.event,
      bgImagePath: imageUrl,
      cityId: Number(formData.event.cityId),
      image: ""
    },
    ticketTypes: formData.eventTicketTypes.ticketTypes,
    eventTicketTypes: []
  });
  return data.data;
};

export const updateEvent = async (id: string, formData: EventCreateGeneral) => {
  const imageUrl = await uploadImage(formData.image);
  const { data } = await api.put(`/event/${id}`, {
    ...formData,
    cityId: Number(formData.cityId),
    bgImagePath: imageUrl,
    image: ""
  });
  return data.code;
};

export const fetchCouponDetail = async (id: string, eventId: string): Promise<Coupon> => {
  const { data } = await api.get(`/coupons?code=${id}&eventId=${eventId}`);
  return data.data;
};

export const fetchCouponsById = async (id: string): Promise<Coupon[]> => {
  const { data } = await api.get(`/coupons/all?eventId=${id}`);
  return data.data;
};

export const fetchCouponDetailById = async (id: number): Promise<Coupon> => {
  const { data } = await api.get(`/coupons/${id}`);
  return data.data;
};

export const createCoupon = async (coupon: CreateCoupon) => {
  if (coupon.id) {
    await api.put(`/coupons`, coupon);
    return;
  }
  const { data } = await  api.post(`/coupons`, coupon);
  return data.data;
};

export const uploadImage = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", "unsigned");
  formData.append("cloud_name", "dj9guhimz");

  const { data } = await axios.post(CLOUDINARY_URL, formData);
  return data.secure_url;
};

export const fetchQueue = async (eventId: string): Promise<number> => {
  const { data } = await api.get(`/event/${eventId}/queue`);
  return data.data;
};

export const fetchOrganizerStats = async (): Promise<Statistic>=> {
  const {data} = await api.get(`/statistic/organizer-dashboard`);
  return data.data;
}