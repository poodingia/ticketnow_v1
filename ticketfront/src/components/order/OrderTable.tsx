import { Table, TableHead, TableHeader, TableRow, TableBody } from "@/components/ui/table"
import type { OrderDetail } from "@/model/order.model"
import OrderTableRow from "@/components/order/OrderTableRow"

interface OrderTableProps {
  orders: OrderDetail[]
}

export default function OrderTable({ orders }: OrderTableProps) {
  return (
    <div className="overflow-x-auto">
      <Table className="w-full">
        <TableHeader>
          <TableRow className="bg-gray-100 dark:bg-gray-800">
            <TableHead className="w-10"></TableHead>
            <TableHead className="font-semibold">Mã đơn hàng</TableHead>
            <TableHead className="font-semibold">Tiêu đề</TableHead>
            <TableHead className="font-semibold">Email</TableHead>
            <TableHead className="font-semibold">Tên</TableHead>
            <TableHead className="font-semibold text-center">Số lượng</TableHead>
            <TableHead className="font-semibold text-center">Giá</TableHead>
            <TableHead className="font-semibold text-center">Thời gian</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {orders?.map((order) => (
            <OrderTableRow order={order} key={order.id} />
          ))}
        </TableBody>
      </Table>
    </div>
  )
}

