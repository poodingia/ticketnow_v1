import { Collapsible, CollapsibleContent } from "@/components/ui/collapsible"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Skeleton } from "@/components/ui/skeleton"
import {useOrderItems} from "@/hooks/useFetch";

interface OrderTableItemRowProps {
  orderId: number
  open: boolean
  setOpen: (open: boolean) => void
}

export default function OrderTableItemRow({ orderId, open, setOpen }: OrderTableItemRowProps) {
  const { data: orderItems, isPending } = useOrderItems(orderId, open);

  return (
    <Collapsible open={open} onOpenChange={setOpen}>
      <CollapsibleContent className="space-y-2 w-100">
        <div className="rounded-md bg-gray-50 dark:bg-gray-800 p-4 overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow className="bg-gray-100 dark:bg-gray-700">
                <TableHead className="font-semibold">Mã Sản Phẩm</TableHead>
                <TableHead className="font-semibold">Mô Tả</TableHead>
                <TableHead className="font-semibold text-right">Số Lượng</TableHead>
                <TableHead className="font-semibold text-right">Giá</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {isPending ? (
                <OrderItemsSkeleton />
              ) : (
                orderItems?.map((orderItem) => (
                  <TableRow key={orderItem.id} className="hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors">
                    <TableCell>{orderItem.id}</TableCell>
                    <TableCell>{orderItem.title}</TableCell>
                    <TableCell className="text-right">{orderItem.quantity}</TableCell>
                    <TableCell className="text-right font-medium">{orderItem.price} VND</TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </div>
      </CollapsibleContent>
    </Collapsible>
  )
}

function OrderItemsSkeleton() {
  return (
    <>
      {[...Array(3)].map((_, index) => (
        <TableRow key={index}>
          <TableCell>
            <Skeleton className="h-4 w-20" />
          </TableCell>
          <TableCell className="text-right">
            <Skeleton className="h-4 w-16 ml-auto" />
          </TableCell>
          <TableCell className="text-right">
            <Skeleton className="h-4 w-24 ml-auto" />
          </TableCell>
        </TableRow>
      ))}
    </>
  )
}

