"use client";

import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import React, { useState } from "react";
import {useParams, useRouter} from "next/navigation";
import {ArrowLeft, Minus, Plus, ShoppingCart} from "lucide-react";
import { Label } from "@/components/ui/label";
import { Separator } from "@/components/ui/separator";
import {useEventDetail, useTicketTypes} from "@/hooks/useFetch";
import Loading from "@/app/loading";
import {formatCurrency} from "@/utils/utils";

export default function Home() {
  const router = useRouter();
  const params = useParams<{ id: string }>()
  const {data: event} = useEventDetail(params.id);
  const [quantities, setQuantities] = useState<{ [key: number]: number }>({});
  const [totalPrice, setTotalPrice] = useState<number>(0);
  const [totalQuantity, setTotalQuantity] = useState<number>(0);
  const { isPending, data: ticketTypes = [] } = useTicketTypes(params.id);

  const handleQuantityChange = (id: number, value: string) => {
    const quantity = Math.max(0, parseInt(value) || 0);
    setQuantities((prev) => {
      const updatedQuantities = { ...prev, [id]: quantity };
      updateTotals(updatedQuantities);
      return updatedQuantities;
    });
  };

  const updateTotals = (updatedQuantities: { [key: number]: number }) => {
    const totalQuantity = Object.values(updatedQuantities).reduce(
      (sum, qty) => sum + qty,
      0
    );
    const totalPrice = ticketTypes.reduce((total, ticket) => {
      const quantity = updatedQuantities[ticket.id] || 0;
      return total + quantity * parseFloat(ticket.price);
    }, 0);
    setTotalQuantity(totalQuantity);
    setTotalPrice(totalPrice);
  };

  const setCheckout = () => {
    sessionStorage.setItem("checkout_price", totalPrice.toString())
    sessionStorage.setItem("checkout_quantity", totalQuantity.toString())
    const orderItems = ticketTypes
      .map((ticketType) => ({
        ticketId: ticketType.id,
        quantity: quantities[ticketType.id] || 0,
        price: Number.parseFloat(ticketType.price) * (quantities[ticketType.id] || 0),
        isType: true,
      }))
      .filter((item) => item.quantity > 0)

    sessionStorage.setItem("checkout_orderItems", JSON.stringify(orderItems))
    router.push("/booking/" + params.id + "/checkout");
  }

  if (isPending) {
    return (
      <Loading />
    )
  }

  return (
    <div className="container max-w-2xl mx-auto px-4 py-16 mb-4">

      <div className="flex items-center justify-between mb-8">
        <Button variant="ghost" onClick={() => router.push(`/`)}>
          <ArrowLeft className="h-4 w-4 mr-2"/>Quay lại trang chủ
        </Button>
      </div>

      <Card className="w-full max-w-3xl mx-auto">
        <CardHeader>
          <CardTitle className="text-2xl font-bold text-center">
            Mua vé cho sự kiện {event?.title}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          {ticketTypes?.map((ticketType) => (
            <div
              key={ticketType.id}
              className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 pb-4"
            >
              <div className="flex-1 space-y-1">
                <p className="text-lg font-semibold">{formatCurrency(Number(ticketType.price))}</p>
                <p className="mt-2">{ticketType.description}</p>
              </div>
              {ticketType.reservedQuantity < ticketType.quantity && <>
                  <Button
                      variant="outline"
                      size="icon"
                      onClick={() => handleQuantityChange(ticketType.id, ((quantities[ticketType.id] || 0) - 1).toString())}
                  >
                      <Minus className="h-4 w-4"/>
                  </Button>
                  <div className="flex items-center space-x-2">
                      <Input
                          type="number"
                          value={quantities[ticketType.id] || 0}
                          onChange={(e) =>
                            handleQuantityChange(ticketType.id, e.target.value)
                          }
                          className="w-12 text-center [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                          min={0}
                      />
                  </div>
                  <Button
                      variant="outline"
                      size="icon"
                      onClick={() => handleQuantityChange(ticketType.id, ((quantities[ticketType.id] || 0) + 1).toString())}
                  >
                      <Plus className="h-4 w-4"/>
                  </Button>
              </>}

            </div>
          ))}
        </CardContent>
        <Separator className="my-4"/>
        <CardFooter className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
          <div className="space-y-1">
            <Label>Tổng số lượng</Label>
            <p className="text-2xl font-bold">{totalQuantity}</p>
          </div>
          <div className="space-y-1">
            <Label>Tổng giá trị</Label>
            <p className="text-2xl font-bold">{formatCurrency(totalPrice)}</p>
          </div>
          <Button className="w-full sm:w-auto" disabled={totalQuantity === 0} onClick={() => setCheckout()}>
            <ShoppingCart className="mr-2 h-4 w-4"/> Đặt ngay
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}
