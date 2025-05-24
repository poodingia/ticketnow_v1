'use client'

import {useOrderStatsOrganizer, useOrdersOrganizer} from "@/hooks/useFetch";
import Loading from "@/app/loading";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import OrderDashboard from "@/components/order/OrderDashboard";
import {Separator} from "@/components/ui/separator";
import OrderTable from "@/components/order/OrderTable";
import type {OrderDetail} from "@/model/order.model";
import {useParams} from "next/navigation";

export default function Home() {
  const params = useParams<{id: string}>();
  const { data: orders, isPending } = useOrdersOrganizer(params.id);
  const { data: orderDashboardData, isPending: isLoading} = useOrderStatsOrganizer(params.id);

  if (isLoading || isPending) {
    return <Loading />;
  }

  return (
    <Card className="mx-auto mt-8 w-[95%] max-w-6xl shadow-lg mb-8">
      <CardHeader className="bg-primary text-primary-foreground">
        <CardTitle className="text-2xl font-bold">Thống kê các đơn hàng của sự kiện</CardTitle>
      </CardHeader>
      <CardContent className="p-6">
        {orderDashboardData && <OrderDashboard orderDashboardData={orderDashboardData}/>}
        <Separator className="m-6"/>
        {isPending ? <Loading /> : <OrderTable orders={orders as OrderDetail[]} />}
      </CardContent>
    </Card>
  )
}