"use client"

import type { OrderDetail } from "@/model/order.model"
import OrderTable from "@/components/order/OrderTable"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import {useOrders, useOrderStats} from "@/hooks/useFetch";
import Loading from "@/app/loading";
import OrderDashboard from "@/components/order/OrderDashboard";
import {Separator} from "@/components/ui/separator";

export default function Home() {
  const { data: orders, isPending } = useOrders();
  const { data: orderDashboardData} = useOrderStats();

  return (
    <Card className="mx-auto mt-8 mb-8 w-[95%] max-w-6xl shadow-lg">
      <CardHeader className="bg-primary text-primary-foreground">
        <CardTitle className="text-2xl font-bold">Đơn hàng của bạn</CardTitle>
      </CardHeader>
      <CardContent className="p-6">
        {orderDashboardData && <OrderDashboard orderDashboardData={orderDashboardData}/>}
        <Separator className="m-6"/>
        {isPending ? <Loading /> : <OrderTable orders={orders as OrderDetail[]} />}
      </CardContent>
    </Card>
  )
}


