import {z} from "zod";

export interface Feedback {
  id: string;
  topic: string;
  message: string;
  status: string
  username: string
}

export interface FeedbackReply {
  id: string;
  feedbackId: string;
  message: string;
  createdAt: string;
  updatedAt: string;
  username: string
}

export const FeedbackCreateSchema = z.object({
  topic: z.string().min(2, {
    message: "Chủ đề phải có ít nhất 2 ký tự.",
  }),
  message: z.string().min(10, {
    message: "Nội dung phải có ít nhất 10 ký tự.",
  }),
});

export type FeedbackCreate = z.infer<typeof FeedbackCreateSchema>