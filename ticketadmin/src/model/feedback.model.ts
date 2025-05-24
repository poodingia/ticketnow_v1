export interface Feedback {
  id: string;
  topic: string;
  message: string;
  status: string;
  createdAt?: string;
  updatedAt?: string;
  username: string;
}

export interface FeedbackReply {
  id: string;
  feedbackId: string;
  message: string;
  createdAt: string;
  updatedAt: string;
  username: string;
}

  export interface UserProfile {
    email: string;
    firstName: string;
    lastName: string;
    isAdmin: boolean
    id: string
  }

