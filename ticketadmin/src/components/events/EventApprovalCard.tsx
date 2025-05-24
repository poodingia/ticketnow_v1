import { EventDetail } from "@/model/event.model";
import React from "react";
import { CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import {Calendar, Clock, List, MapPin} from "lucide-react";
import Image from "next/image";

interface EventApprovalCardProps {
  event: EventDetail;
  onClick?: () => void;
}

export default function EventApprovalCard(props: EventApprovalCardProps) {
  const { event } = props;

  return (
    <CardContent>
      <CardHeader>
        <div className="flex flex-col md:flex-row gap-6">
          <div className="w-full md:w-2/3">
            <Image
              src={event.bgImagePath} alt={event.title} width={600} height={400}
              className="rounded-lg object-cover w-full h-[200px]"
            />
          </div>
          <div className="w-full md:w-1/3">
            <CardTitle className="text-3xl pb-3">{event.title}</CardTitle>
            <CardDescription>
              <div className="flex flex-col md:flex-row space-y-2 md:space-y-0 md:space-x-4">
                <div className="flex flex-col md:w-1/2 gap-3">
                  <div className="flex items-center text-sm">
                    <Calendar className="w-5 h-5 mr-2"/>
                    <span>{new Date(event.date).toLocaleDateString()}</span>
                  </div>
                  <div className="flex items-center">
                    <Clock className="w-5 h-5 mr-2"/>
                    <span>Mở bán: {new Date(event.startSaleDate).toLocaleDateString()}</span>
                  </div>
                  <div className="flex items-center">
                    <Clock className="w-5 h-5 mr-2"/>
                    <span>Đóng: {new Date(event.endSaleDate).toLocaleDateString()}</span>
                  </div>
                </div>
                <div className="flex flex-col md:w-1/2 gap-3">
                  <div className="flex items-center">
                    <MapPin className="w-5 h-5 mr-2"/>
                    <span>{event.location}</span>
                  </div>
                  <div className="flex items-center">
                    <List className="w-5 h-5 mr-2"/>
                    <span>Sức chứa: {event.bookingCapacity}</span>
                  </div>
                </div>
              </div>
            </CardDescription>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {event.description && <div dangerouslySetInnerHTML={{__html: event.description}}
                                   className="leading-7 [&:not(:first-child)]:mt-6"/>}
      </CardContent>
    </CardContent>
  );
}