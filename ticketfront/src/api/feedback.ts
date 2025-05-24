import { API_FEEDBACK_URL} from "@/utils/constant";
import {Feedback, FeedbackCreate, FeedbackReply} from "@/model/feedback.model";
import axios from 'axios';

const api = axios.create({
  baseURL: API_FEEDBACK_URL,
  headers: { "Content-Type": "application/json" }
});

export const getFeedbackReplies = async (id: string): Promise<FeedbackReply[]> => {
  const { data } = await api.get(`/${id}/feedback-replies`);
  return data.data;
}

export const getFeedbacks = async (): Promise<Feedback[]> => {
  const { data } = await api.get(``);
  return data.data;
}

export const getFeedbackDetail = async (id: string): Promise<Feedback> => {
  const { data } = await api.get(`/${id}`);
  return data.data;
}

export const createFeedback = async (feedback: FeedbackCreate): Promise<Feedback> => {
  const { data } = await api.post(``, feedback);
  return data.data;
};

export const updateFeedback = async (id: string, status: string): Promise<Feedback> => {
  const { data } = await api.put(`/${id}/status`, { status });
  return data.data;
};

export const postFeedbackReply = async (id: string, message: string) => {
  const { data } = await api.post(`/${id}/feedback-replies`, { feedbackId: Number(id), message });
  return data.code;
}