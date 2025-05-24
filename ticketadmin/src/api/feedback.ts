import axios from 'axios';
import {Feedback, FeedbackReply, UserProfile} from "@/model/feedback.model";
import {API_ACCOUNT_URL, API_FEEDBACK_URL} from "@/utils/constant";

export const getFeedbackDetail = async (id: string): Promise<Feedback> => {
  const response = await axios.get(`${API_FEEDBACK_URL}/${id}`);
  return response.data.data;
}

export const getFeedbacks = async (): Promise<Feedback[]> => {
  const response = await axios.get(`${API_FEEDBACK_URL}/all`);
  return response.data.data;
}

export const getFeedbackReplies = async (id: string): Promise<FeedbackReply[]> => {
  const response = await axios.get(`${API_FEEDBACK_URL}/${id}/feedback-replies`);
  return response.data.data;
}

export const postFeedbackReply = async (id: string, message: string) => {
  const response = await axios.post(`${API_FEEDBACK_URL}/${id}/feedback-replies`, {
    feedbackId: Number(id),
    message
  }, {
    headers: {"Content-Type": "application/json"}
  });
  return response.data.code;
}

export const getUserProfile = async (email: string) :Promise<UserProfile>=> {
  const response = await axios.get(`${API_ACCOUNT_URL}`, {params: { email }});
  return response.data.data;
}

export const getAllUserProfiles = async (): Promise<UserProfile[]> => {
  const response = await axios.get(`${API_ACCOUNT_URL}/all?page=0`);
  return response.data.data;
}

export const deleteUserProfile = async (id: string) => {
  const response = await axios.delete(`${API_ACCOUNT_URL}/${id}`);
  return response.data.code;
}

export const updateUserRoles = async (email: string) => {
  const response = await axios.put(`${API_ACCOUNT_URL}/${email}`);
  return response.data.data;
}