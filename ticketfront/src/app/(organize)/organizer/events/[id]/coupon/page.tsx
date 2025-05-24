"use client"

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {useCoupons, useEventDetail} from "@/hooks/useFetch";
import {useParams} from "next/navigation";
import CouponModal from "@/components/events/CouponModal";
import {useState} from "react";
import {formatCurrency} from "@/utils/utils";

export default function CouponList() {
  const params = useParams<{id: string}>();
  const {data: coupons} = useCoupons(params.id);
  const {data: event} = useEventDetail(params.id);
  const [open, setOpen] = useState(false)

  return (
    <div className="w-full max-w-4xl mx-auto p-4 space-y-4">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold">Mã giảm giá</h2>
          <p className="text-muted-foreground">Quản lý mã giảm giá cho sự kiện {event?.title}</p>
        </div>
        <CouponModal open={open} setOpen={setOpen} id={params.id}/>
      </div>

      <div className="border rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Mã</TableHead>
              <TableHead>Giá trị</TableHead>
              <TableHead>Số lượng</TableHead>
              <TableHead>Đã dùng</TableHead>
              <TableHead>Loại</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {coupons?.map((coupon) => (
              <TableRow key={coupon.id}>
                <TableCell className="font-medium">{coupon.code}</TableCell>
                <TableCell>{coupon.category === "RATE" ? `${coupon.value}%` : formatCurrency(coupon.value)}</TableCell>
                <TableCell>{coupon.quantity}</TableCell>
                <TableCell>{coupon.used}</TableCell>
                <TableCell>
                  <span
                    className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      coupon.category === "FIXED"
                        ? "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300"
                        : "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300"
                    }`}
                  >
                    {coupon.category === "FIXED" ? "Cố định" : "Tỉ lệ"}
                  </span>
                </TableCell>
                <TableCell><CouponModal open={open} setOpen={setOpen} id={params.id} coupon={coupon}/></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}

