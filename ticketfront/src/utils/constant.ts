export const API_CRUD_URL = process.env.NEXT_PUBLIC_GATEWAY_URL +`/api/crud`;
export const API_ORDER_URL = process.env.NEXT_PUBLIC_GATEWAY_URL +`/api/order`;
export const API_ACCOUNT_URL =process.env.NEXT_PUBLIC_GATEWAY_URL +`/api/account`
export const API_FOLLOW_URL =process.env.NEXT_PUBLIC_GATEWAY_URL +`/api/follow`
export const API_FEEDBACK_URL = process.env.NEXT_PUBLIC_GATEWAY_URL +`/api/feedback`;
export const API_SEARCH_URL = process.env.NEXT_PUBLIC_GATEWAY_URL +`/api/search`;
export const CLOUDINARY_URL = `https://api.cloudinary.com/v1_1/${process.env.NEXT_PUBLIC_CLOUDINARY_CLOUD_NAME}/upload`;

export const QUEUE_REFETCH_INTERVAL = 10000;