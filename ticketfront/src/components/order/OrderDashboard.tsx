import {DollarSign, Package, ShoppingCart} from "lucide-react"

import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card"
import {OrderDashboardData} from "@/model/order.model";
import {formatCurrency} from "@/utils/utils";

interface OrderDashboardProps {
  orderDashboardData: OrderDashboardData
}

export default function OrderDashboard({ orderDashboardData }: OrderDashboardProps) {
  const { period, totalExpense, totalOrder, totalQuantity } = orderDashboardData

  const formatNumber = (num: number) => {
    return new Intl.NumberFormat("en-US").format(num)
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Tổng quan</h2>
        <p className="text-muted-foreground">{period}</p>
      </div>

      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">Tổng giá trị đơn hàng</CardTitle>
        <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
        <div className="text-2xl font-bold">{formatCurrency(totalExpense)}</div>
        <p className="text-xs text-muted-foreground mt-1">Tổng giá trị tiền tệ của tất cả các đơn hàng</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">Tổng số đơn hàng</CardTitle>
        <ShoppingCart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
        <div className="text-2xl font-bold">{formatNumber(totalOrder)}</div>
        <p className="text-xs text-muted-foreground mt-1">Số lượng giao dịch đơn hàng</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">Tổng số lượng</CardTitle>
        <Package className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
        <div className="text-2xl font-bold">{formatNumber(totalQuantity)}</div>
        <p className="text-xs text-muted-foreground mt-1">Tổng số vé đã mua</p>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

