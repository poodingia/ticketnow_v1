"use client"

import type { OrderDetail } from "@/model/order.model"
import { TableCell, TableRow } from "@/components/ui/table"
import { useState, useCallback } from "react"
import { Button } from "@/components/ui/button"
import { ChevronDown, ChevronRight } from "lucide-react"
import OrderTableItemRow from "@/components/order/OrderTableItemRow"
import {useCouponDetailById} from "@/hooks/useFetch";
import {formatDate} from "@/utils/utils";

interface OrderTableRowProps {
  order: OrderDetail
}

export default function OrderTableRow({ order }: OrderTableRowProps) {
  const [isExpanded, setIsExpanded] = useState<boolean>(false)
  const {data: coupon} = useCouponDetailById(Number(order.couponCode));

  const toggleExpansion = useCallback(() => {
    setIsExpanded((prev) => !prev)
  }, [])


  return (
    <>
      <TableRow className="hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors">
        <TableCell className="w-10">
          <Button
            variant="ghost"
            size="sm"
            className="h-8 w-8 p-0 transition-transform duration-200"
            onClick={toggleExpansion}
            aria-expanded={isExpanded}
            aria-label={isExpanded ? "Collapse order details" : "Expand order details"}
          >
            {isExpanded ? (
              <ChevronDown className="h-4 w-4 text-primary" />
            ) : (
              <ChevronRight className="h-4 w-4 text-primary" />
            )}
          </Button>
        </TableCell>
        <TableCell className="font-medium">{order.id}</TableCell>
        <TableCell className="font-medium">{order.eventTitle}</TableCell>
        <TableCell className="max-w-[200px] truncate">{order.email}</TableCell>
        <TableCell>{order.name}</TableCell>
        <TableCell className="text-center">{order.quantity}</TableCell>
        <TableCell className="text-center font-semibold">{order.price} VND</TableCell>
        <TableCell className="text-center">{order.createdAt && formatDate(order.createdAt.slice(0,-1))}</TableCell>

      </TableRow>
      {isExpanded && (
        <TableRow>
          <TableCell colSpan={8} className="p-0">
            <div
              className="w-full"
              style={{ maxHeight: isExpanded ? "500px" : "0" }}
            >
              <OrderTableItemRow open={isExpanded} orderId={order.id} setOpen={setIsExpanded} />
              {order.couponCode && coupon && (
                <div className="m-4 border-t pt-4">
                  <h4 className="text-sm font-semibold mb-2">Mã giảm gía đã sử dụng</h4>
                  <div className="space-y-2 text-sm">
                    <div className="flex justify-between items-center">
                      <span className="text-muted-foreground">Mã:</span>
                      <span className="font-medium">{coupon.code}</span>
                    </div>
                    <div className="flex justify-between items-center text-green-600">
                      <span>Giảm giá:</span>
                      {
                        coupon.category === "RATE" ? (
                          <span>- {coupon.value}%</span>
                        ) : (
                          <span>-   {coupon.value} VND</span>
                        )
                      }
                    </div>
                    <div className="flex justify-between items-center font-semibold">
                      <span>Giá cuối:</span>
                      <span>{order.price} VND</span>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </TableCell>
        </TableRow>
      )}
    </>
  )
}

