export interface EventDetail {
  max?: number;
  id: number;
  title: string;
  location: string;
  date: string; // Corrected type
  image: string;
  bgImagePath: string;
  description?: string;
  isApproved?: boolean;
  bookingCapacity?: number;
  startSaleDate: string;
  endSaleDate: string;
}

export interface Statistic {
  totalEvents: number;
  totalTicketsSold: number;
  totalRevenue: number;
  topFiveRevenueEvents: {name: string, revenue: number}[];
  topFiveTicketTypes: {name: string, quantity: number}[];
}
