import * as z from "zod";

export const formSchema = z.object({
  id: z.string(),
  username: z.string().min(2, {
    message: "Tên người dùng phải có ít nhất 2 ký tự.",
  }),
  email: z.string().email({
    message: "Vui lòng nhập địa chỉ email hợp lệ.",
  }),
  firstName: z.string().min(1, {
    message: "Tên là bắt buộc.",
  }),
  lastName: z.string().min(1, {
    message: "Họ là bắt buộc.",
  }),
})

export type CustomerProfile = z.infer<typeof formSchema>