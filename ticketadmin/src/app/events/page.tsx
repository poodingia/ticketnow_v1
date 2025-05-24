"use client"

import { useState } from "react"
import { ArrowRight, CalendarIcon, MapPin } from "lucide-react"
import Link from "next/link"

import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Skeleton } from "@/components/ui/skeleton"
import { Button } from "@/components/ui/button"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { useEvents } from "@/hooks/useFetch"

export default function Home() {
  const [filter, setFilter] = useState<string>("true")

  const { data: events, isLoading } = useEvents()

  const filteredEvents = events?.filter((event) => {
    if (filter === "all") return true
    return filter === "true" ? event.isApproved : !event.isApproved
  })

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold mb-6">Events</h1>
        <div className="space-y-4">
          {[1, 2, 3].map((i) => (
            <Card key={i} className="animate-pulse">
              <CardContent className="p-6">
                <Skeleton className="h-6 w-1/3 mb-4" />
                <Skeleton className="h-4 w-1/4 mb-2" />
                <Skeleton className="h-4 w-1/5" />
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    )
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Quản lý sự kiện</h1>
        <div className="flex justify-between items-center">
          <Select onValueChange={(value) => setFilter(value)}>
            <SelectTrigger className="w-48">
              <SelectValue placeholder="Đã được duyệt" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Tất cả</SelectItem>
              <SelectItem value="true">Đã dược duyệt</SelectItem>
              <SelectItem value="false">Chưa được duyệt</SelectItem>
            </SelectContent>
          </Select>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {filteredEvents?.map((event) => (
          <Card key={event.id} className="group hover:shadow-lg transition-all duration-300 flex flex-col h-full">
            <CardHeader>
              <CardTitle className="text-xl line-clamp-1">{event.title}</CardTitle>
            </CardHeader>
            <CardContent className="flex-grow">
              <div className="space-y-2 text-sm text-muted-foreground">
                <div className="flex items-center gap-2">
                  <CalendarIcon className="w-4 h-4" />
                  <span>
                    {new Date(event.date).toLocaleDateString("vi-VN", {
                      weekday: "long",
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                    })}
                  </span>
                </div>
                <div className="flex items-center gap-2">
                  <MapPin className="w-4 h-4" />
                  <span>{event.location}</span>
                </div>
              </div>
            </CardContent>
            <CardFooter className="mt-auto pt-4">
              <Link href={`/events/${event.id}`} className="w-full">
                <Button className="w-full group-hover:translate-x-1 transition-transform" variant="default">
                  Xem chi tiết
                  <ArrowRight className="w-4 h-4 ml-2" />
                </Button>
              </Link>
            </CardFooter>
          </Card>
        ))}
      </div>

      {filteredEvents?.length === 0 && (
        <Card className="p-8 text-center">
          <p>Không có sự kiện nào</p>
        </Card>
      )}
    </div>
  )
}
