'use client'

import {useParams, useRouter} from "next/navigation";
import {approveEvent, deleteEvent} from "@/api/event";
import EventApprovalCard from "@/components/events/EventApprovalCard";
import { useToast } from "@/hooks/use-toast";
import TicketTypeTable from "@/components/ticketTypes/TicketTypeTable";
import { Button } from "@/components/ui/button";
import React from "react";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Trash, Check } from "lucide-react";
import {useEventDetail, useTicketTypes} from "@/hooks/useFetch";
import EventBookingCapacityModal from "@/components/events/EventBookingCapacityCard";

export default function Home() {
  const router = useRouter();
  const { toast } = useToast();
  const params = useParams<{ id: string }>();

  const { data: event, isPending: eventLoading } = useEventDetail(params.id);
  const { data: ticketTypes, isPending: ticketTypeLoading } = useTicketTypes(params.id)

  const onApprove = async () => {
    const code = await approveEvent(event.id);
    if (code === '00000') {
      toast({title: "Thành công",});
    } else {
      toast({title: "Thất bại",});
    }
    router.push('/events');
  };

  const onDelete = async () => {
    const code = await deleteEvent(event.id);
    if (code === '00000') {
      toast({title: "Đã xóa",});
    } else {
      toast({title: "Thất bại",});
    }
    router.push('/events');
  };



  if (eventLoading || ticketTypeLoading) {
    return <EventCardSkeleton />;
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <Card className="p-6 rounded-lg border border-gray-200 transition-shadow hover:shadow-lg">
        <EventApprovalCard event={event} onClick={onApprove} />
        <TicketTypeTable ticketTypes={ticketTypes} />
        <CardFooter className="flex justify-end space-x-4 mt-4">
          <EventBookingCapacityModal id={params.id}/>
          <Button onClick={onApprove} className="flex items-center" disabled={event.isApproved}>
            <Check className="mr-2" /> {event.isApproved ? "Đã duyệt" : "Duyệt"}
          </Button>
          <Button onClick={onDelete} className="bg-red-500 text-white flex items-center">
            <Trash className="mr-2" /> Xóa
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}

function EventCardSkeleton() {
  return (
    <Card className="w-full p-6 rounded-lg border border-gray-200">
      <CardHeader>
        <Skeleton className="h-6 w-3/4" />
      </CardHeader>
      <CardContent>
        <Skeleton className="h-4 w-full mb-2" />
        <Skeleton className="h-4 w-2/3" />
      </CardContent>
      <CardFooter>
        <Skeleton className="h-10 w-full" />
      </CardFooter>
    </Card>
  );
}