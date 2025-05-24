import {z} from "zod";
import {TicketTypeListCreate} from "@/model/ticketType.model";

export interface EventSummary {
  id: string;
  title: string;
  date: string;
  bgImagePath: string;
  description: string;
  location?: string;
}

export interface Statistic {
  totalEvents: number;
  totalTicketsSold: number;
  totalRevenue: number;
  topFiveRevenueEvents: {name: string, revenue: number}[];
  topFiveTicketTypes: {name: string, quantity: number}[];
}

export interface EventDetail {
  max?: number;
  id: number;
  title: string;
  location: string;
  date: string;
  image: string;
  bgImagePath: string;
  description: string;
  startSaleDate: string;
  endSaleDate: string;
  category: string;
  cityId: string;
  isApproved?: boolean
}

export interface Coupon {
  id: number
  code: string;
  value: number;
  category: string;
  quantity: number;
  used? : number
}

export interface CreateCoupon {
  id?: number
  code: string;
  value: number;
  category: string;
  eventId: number;
  quantity: number;
}

export const eventCreateSchema = z.object({
  id: z.number().optional(),
  title: z.string().min(2, { message: "Tiêu đề sự kiện phải có ít nhất 2 ký tự." }),
  description: z.string().min(10, { message: "Mô tả phải có ít nhất 10 ký tự." }),
  category: z.string().nonempty({ message: "Thể loại là bắt buộc." }),
  location: z.string().min(2, { message: "Địa điểm phải có ít nhất 2 ký tự." }),
  date: z.string().nonempty({ message: "Ngày tổ chức là bắt buộc." }),
  startSaleDate: z.string().min(1, { message: "Ngày bắt đầu bán vé là bắt buộc." }),
  endSaleDate: z.string().nonempty({ message: "Ngày kết thúc bán vé là bắt buộc." }),
  cityId: z.string().nonempty({ message: "Tỉnh là bắt buộc." }),
  image: z.any().refine((files) => files?.length >= 1, { message: 'Cần có hình ảnh sự kiện' }),
  bgImagePath: z.any().optional(),
  max: z.any(),
}).refine(
  (data) => new Date(data.startSaleDate) < new Date(data.endSaleDate),  {
    message: "Ngày không hợp lệ",
    path: ["startSaleDate"],
  }
).refine(
  (data) => new Date(data.endSaleDate) < new Date(data.date), {
    message: "Ngày không hợp lệ",
    path: ["endSaleDate"],
  }
);

export type EventCreateGeneral = z.infer<typeof eventCreateSchema>


export interface EventCreateForm {
  event: EventCreateGeneral;
  eventTicketTypes: TicketTypeListCreate;
}

export interface EventCreateStepProps {
  formData: EventCreateForm,
  updateFormData(stepData: EventCreateGeneral | TicketTypeListCreate): void
}