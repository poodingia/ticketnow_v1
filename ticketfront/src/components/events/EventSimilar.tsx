"use client"

import { useSimilarEvents } from "@/hooks/useFetch"
import EventCard from "@/components/events/EventCard"

export default function EventSimilar({ title }: { title: string }) {
  const { data: similarEvents } = useSimilarEvents(title)

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mx-auto">
      {similarEvents?.slice(0, 4).map((event) => (
        <div className="w-full" key={event.id}>
          <EventCard event={event} />
        </div>
      ))}
    </div>
  )
}