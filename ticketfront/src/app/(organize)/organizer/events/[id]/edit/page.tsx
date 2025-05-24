import TicketTypeListCard from "@/components/ticketTypes/TicketTypeListCard";
import EventEditCard from "@/components/events/EventEditCard";
import {fetchEventDetail} from "@/api/event";
import {fetchTicketTypes} from "@/api/ticketType";
import {getALlCities} from "@/api/city";
import {Suspense} from "react";

export default async function Home({ params }: { params: { id: string } }) {
  const event = await fetchEventDetail(params.id);
  const ticketTypes = await fetchTicketTypes(params.id);
  const cities = await getALlCities();

  return (
    <Suspense>
      <div className="space-y-4" key={params.id}>
        <EventEditCard id={params.id} event={event} cities={cities}/>
        <TicketTypeListCard id={params.id} ticketTypes={ticketTypes}/>
      </div>
    </Suspense>
  );
}