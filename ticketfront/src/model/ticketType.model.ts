import * as z from "zod";

export interface TicketType {
  id: number;
  price: string;
  description: string;
  quantity: number;
  reservedQuantity: number
}

export const ticketTypeCreateSchema = z.object({
  description: z.string().min(1, "Tên loại vé là bắt buộc."),
  price: z.any(),
  quantity: z.any(),
  id: z.number().default(0)
})
export const ticketTypeListCreateSchema = z.object({
  ticketTypes: z.array(ticketTypeCreateSchema).min(1, "Vui lòng thêm ít nhất một loại vé."),
})

export type TicketTypeListCreate = z.infer<typeof ticketTypeListCreateSchema>
export type TicketTypeCreate = z.infer<typeof ticketTypeCreateSchema>

