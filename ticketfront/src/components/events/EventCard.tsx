import Image from "next/image"
import Link from "next/link"
import {EventSummary} from "@/model/event.model";
import {CalendarIcon, MapPinIcon} from "lucide-react";
import {generateSlug} from "@/utils/utils";

export default function EventCard({ event }: { event: EventSummary}) {
    const formattedDate = new Date(event.date).toLocaleDateString("vi-VN", {
        month: "short",
        day: "numeric",
        year: "numeric",
    })

    return (
      <Link href={`/events/${generateSlug(event.title)}-${String(event.id)}`} className="block w-[350px]">
          <div className={`overflow-hidden group h-[380px] transition-all duration-300 mb-6`}>
              <div className="relative w-full h-full">
                  <div className="relative h-[220px] overflow-hidden">
                      <Image
                        src={event.bgImagePath || "/placeholder.svg"} alt={event.title}
                        fill style={{ objectFit: "cover" }}
                        sizes="(min-width: 768px) 33vw, (min-width: 640px) 50vw, 100vw"
                        className="rounded-xl" priority
                      />
                  </div>
                  <div className="p-5 flex flex-col h-[160px]">
                      <span className="text-xl font-semibold line-clamp-2 group-hover:text-primary transition-colors">
                          {event.title}
                      </span>

                      <div className="mt-auto space-y-2 text-sm text-muted-foreground">
                          <div className="flex items-center gap-2">
                              <CalendarIcon className="h-4 w-4" />
                              <span>{formattedDate}</span>
                          </div>
                          <div className="flex items-center gap-2">
                              <MapPinIcon className="h-4 w-4" />
                              <span className="truncate">{event.location}</span>
                          </div>

                      </div>
                  </div>
              </div>
          </div>
      </Link>
    )
}
