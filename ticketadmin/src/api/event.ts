import axios from "axios";
import { EventDetail, Statistic } from "@/model/event.model";
import { API_CRUD_URL, CLOUDINARY_URL } from "@/utils/constant";

const api = axios.create({ baseURL: API_CRUD_URL });

export const uploadImage = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", "unsigned");
  formData.append("cloud_name", "dj9guhimz");

  const { data } = await axios.post(CLOUDINARY_URL, formData);
  return data.secure_url;
};

export const fetchEvents = (endpoint: string): Promise<EventDetail[]> =>
  api.get(`/event${endpoint}`).then(({ data }) => data.data);

export const fetchEventDetail = (endpoint: string) =>
  api.get(`/event/${endpoint}`).then(({ data }) => data.data);

export const approveEvent = (id: string) =>
  api.put(`/event/${id}/approval`).then(({ data }) => data.code);

export const updateBookingCapacity = (id: string, capacity: number) =>
  api.put(`/event/${id}/queue`, { number: capacity }).then(({ data }) => data.code);

export const deleteEvent = (id: string) =>
  api.delete(`/event/${id}`).then(({ data }) => data.code);

export const fetchAdminStats = (): Promise<Statistic> =>
  api.get(`/statistic/admin-dashboard`).then(({ data }) => data.data);
