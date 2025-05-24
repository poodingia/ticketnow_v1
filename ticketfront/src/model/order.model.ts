export interface OrderItem {
  id?: number;
  ticketId: number;
  quantity: number;
  price: number;
  isType: boolean;
  title?: string;
}

export interface OrderDetail {
  id: number;
  price: number;
  quantity: number;
  userId: number;
  orderItems: OrderItem[];
  email: string;
  name: string;
  status?: string;
  couponCode?: string;
  eventId?: number;
  paymentMethod?: string;
  eventTitle?: string;
  createdAt?: string
}

export interface PaymentStatusData {
  code: string;
  ref: string;
  status?: string;
}

export interface OrderDashboardData {
  totalExpense: number
  totalOrder: number
  totalQuantity: number
  customerName?: string
  period?: string
}