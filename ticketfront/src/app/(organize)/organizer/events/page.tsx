"use client"

import { CalendarIcon, MapPinIcon, PlusIcon } from "lucide-react"
import { buttonVariants } from "@/components/ui/button"
import Link from "next/link"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Skeleton } from "@/components/ui/skeleton"
import { useOrganizerEvents } from "@/hooks/useFetch"
import { formatDate } from "@/utils/utils"
import { Badge } from "@/components/ui/badge"

export default function OrganizerEventsPage() {
  const { data: events = [], isPending } = useOrganizerEvents()

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8">
        <h1 className="text-3xl font-bold mb-4 sm:mb-0">Sự kiện của bạn</h1>
        <Link href={"/organizer/create"} className={buttonVariants({ variant: "default" })}>
          <PlusIcon className="w-5 h-5 mr-2" />
          Tạo thêm sự kiện mới
        </Link>
      </div>

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        {isPending
          ? Array.from({ length: 6 }).map((_, index) => (
            <Card key={index} className="w-full flex flex-col min-h-[250px]">
              <CardHeader>
                <Skeleton className="h-6 w-3/4" />
              </CardHeader>
              <CardContent className="flex-grow">
                <Skeleton className="h-4 w-full mb-2" />
                <Skeleton className="h-4 w-2/3" />
              </CardContent>
              <CardFooter>
                <Skeleton className="h-10 w-full" />
              </CardFooter>
            </Card>
          ))
          : events.map((event) => (
            <Card key={event.id} className="w-full flex flex-col min-h-[250px]">
              <CardHeader>
                <CardTitle className="flex justify-between">
                  <Link href={`/organizer/events/${event.id}`}>{event.title}</Link>
                  <Badge className="min-w-[90px] text-center max-h-[25px]">{event.isApproved ? "Đã duyệt" : "Chưa duyệt"}</Badge>
                </CardTitle>
              </CardHeader>
              <CardContent className="flex-grow">
                <div className="flex flex-col space-y-2 text-sm text-muted-foreground">
                  <div className="flex items-center">
                    <CalendarIcon className="w-4 h-4 mr-2" />
                    {formatDate(event.date)}
                  </div>
                  <div className="flex items-center">
                    <MapPinIcon className="w-4 h-4 mr-2" />
                    {event.location}
                  </div>
                </div>
              </CardContent>
              <CardFooter className='flex gap-3'>
              <Link href={`/organizer/events/${event.id}/coupon`} className={buttonVariants({ variant: "default" })}>
                Mã giảm giá
              </Link>
              <Link href={`/organizer/events/${event.id}/edit`} className={ buttonVariants({ variant: "default" })}>
                Chỉnh sửa
              </Link>
            </CardFooter>
            </Card>
          ))}
      </div>
    </div>
  )
}
