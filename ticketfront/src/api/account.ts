import {API_ACCOUNT_URL, API_FOLLOW_URL} from "@/utils/constant";
import {CustomerProfile} from "@/model/account.model";
import axios from 'axios';

const api = axios.create({
  baseURL: API_ACCOUNT_URL,
  headers: { "Content-Type": "application/json" }
});

export const getCustomerProfile = async () => {
  const { data } = await api.get(`/me`);
  return data.data;
}

export const updateCustomerProfile = async (customer: CustomerProfile) => {
  const { data } = await api.put(`/me`, customer);
  return data;
}

export const updateFollowEvent = async (eventId: string, isFollowing: boolean) => {
  const method = isFollowing ? 'delete' : 'post';
  const { data } = await axios[method](`${API_FOLLOW_URL}/${eventId}`);
  return data;
}

export const checkFollowEvent = async (eventId: string): Promise<boolean> => {
  const { data } = await axios.get(`${API_FOLLOW_URL}/${eventId}`);
  return data.data;
}
