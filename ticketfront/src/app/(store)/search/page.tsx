import {searchEvents} from "@/api/search";
import EventCard from "@/components/events/EventCard";

export default async function Home({searchParams} : {searchParams: {q? : string, category? : string}}) {
  const q = searchParams.q || ''
  const category = searchParams.category || ''
  const events = await searchEvents(q, category)

  return (
    <div className="container mx-auto px-4 py-12">
      <h1 className="text-4xl font-bold mb-8 text-center">Kết quả tìm kiếm</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        {events?.map((event) => (
          <EventCard event={event} key={event.id}/>
        ))}
      </div>
    </div>
  )

}