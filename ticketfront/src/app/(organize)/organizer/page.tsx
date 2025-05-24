"use client"

import { DollarSign, Ticket} from "lucide-react"
import {Bar, BarChart as RechartsBarChart, CartesianGrid, Cell, ResponsiveContainer, Tooltip, XAxis, YAxis,} from "recharts"
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card"
import {useOrganizeStats} from "@/hooks/useFetch";
import {formatCurrency} from "@/utils/utils";
import Loading from "@/app/loading";
import Link from "next/link";
import {Button} from "@/components/ui/button";

export default function Dashboard() {
  const {data: dashboardData, isPending} = useOrganizeStats()
  if (isPending || !dashboardData) {
    return <Loading/>
  }

  return (
  <div className="min-h-screen w-full bg-gradient-to-b from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800">
    <div className="flex flex-col">
      <header className="sticky top-0 z-10 backdrop-blur-lg bg-white/60 dark:bg-gray-900/60 border-b border-gray-200 dark:border-gray-800">
        <div className="container mx-auto px-4">
          <div className="flex h-16 items-center justify-between">
            <h1 className="text-2xl font-bold">
              Thống kê doanh thu sự kiện của bạn
            </h1>
            <div className="flex items-center gap-2 text-sm text-muted-foreground">
              <Link href={'/organizer/events'}>
                <Button>Quản lý sự kiện</Button>
              </Link>
            </div>
          </div>
        </div>
      </header>

      <main className="container mx-auto flex-1 space-y-6 p-4 md:p-8">
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          <Card className="hover:shadow-lg transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium">Tổng doanh thu</CardTitle>
              <DollarSign className="h-4 w-4 text-muted-foreground"/>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold tracking-tight text-primary">
                {formatCurrency(dashboardData?.totalRevenue ?? null)}
              </div>
            </CardContent>
          </Card>
          <Card className="hover:shadow-lg transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium">Số vé đã bán</CardTitle>
              <Ticket className="h-4 w-4 text-muted-foreground"/>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold tracking-tight text-primary">
                {dashboardData?.totalTicketsSold.toLocaleString()}
              </div>
            </CardContent>
          </Card>
          <Card className="hover:shadow-lg transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between pb-2">
              <CardTitle className="text-sm font-medium">Giá vé trung bình</CardTitle>
              <DollarSign className="h-4 w-4 text-muted-foreground"/>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold tracking-tight text-primary">
                {formatCurrency(dashboardData.totalRevenue / dashboardData.totalTicketsSold)}
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="grid gap-6 md:grid-cols-7">
          <Card className="md:col-span-7 hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle>Doanh thu từng sự kiện</CardTitle>
              <CardDescription>Top sự kiện theo doanh thu</CardDescription>
            </CardHeader>
            <CardContent className="pl-2">
              <ResponsiveContainer width="100%" height={400}>
                <RechartsBarChart
                  data={dashboardData.topFiveRevenueEvents}
                  margin={{top: 20, right: 30, left: 40, bottom: 70}}
                >
                  <CartesianGrid strokeDasharray="3 3" vertical={false} className="opacity-30"/>
                  <XAxis
                    dataKey="name" angle={-45} textAnchor="end" height={70}
                    tickFormatter={(value) => (value.length > 20 ? `${value.substring(0, 20)}...` : value)}
                    tick={{fill: "currentColor", fontSize: 12}}
                  />
                  <YAxis
                    tickFormatter={(value) => `${value / 1000}k đ`}
                    tick={{fill: "currentColor", fontSize: 12}}
                  />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "rgba(255, 255, 255, 0.95)", border: "none", borderRadius: "8px",
                      boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)",
                    }}
                    formatter={(value: number) => [`${formatCurrency(value)}`, "Doanh thu"]}
                  />
                  <Bar dataKey="revenue" radius={[6, 6, 0, 0]} barSize={50}>
                    {dashboardData.topFiveRevenueEvents.map((_, index) => (<Cell
                      key={`cell-${index}`}
                      className="opacity-80 hover:opacity-100 transition-opacity"
                    />))}
                  </Bar>
                </RechartsBarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

        </div>

        <div className="grid gap-6 md:grid-cols-7">

          <Card className="md:col-span-7 hover:shadow-lg transition-shadow">
            <CardHeader>
              <CardTitle>Số lượng từng loại vé</CardTitle>
              <CardDescription>Top loại vé theo số lượng</CardDescription>
            </CardHeader>
            <CardContent className="pl-2">
              <ResponsiveContainer width="100%" height={400}>
                <RechartsBarChart
                  data={dashboardData.topFiveTicketTypes}
                  margin={{top: 20, right: 30, left: 40, bottom: 70}}
                >
                  <CartesianGrid strokeDasharray="3 3" vertical={false} className="opacity-30"/>
                  <XAxis
                    dataKey="name" angle={-45} textAnchor="end" height={70}
                    tick={{fill: "currentColor", fontSize: 12}}
                  />
                  <YAxis
                    tickFormatter={(value) => `${value} vé`}
                    tick={{fill: "currentColor", fontSize: 12}}
                    allowDecimals={false  }
                  />
                  <Tooltip
                    contentStyle={{
                      backgroundColor: "rgba(255, 255, 255, 0.95)", border: "none", borderRadius: "8px",
                      boxShadow: "0 4px 12px rgba(0, 0, 0, 0.1)",
                    }}
                    formatter={(value: number) => [`${value}`, "Số lượng"]}
                  />
                  <Bar dataKey="quantity" radius={[6, 6, 0, 0]} barSize={50}>
                    {dashboardData.topFiveTicketTypes.map((_, index) => (<Cell
                      key={`cell-${index}`}
                      className="opacity-80 hover:opacity-100 transition-opacity"
                    />))}
                  </Bar>
                </RechartsBarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  </div>)
}

