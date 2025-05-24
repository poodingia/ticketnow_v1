'use client'

import EventCard from "../events/EventCard";
import EventScroll from "../events/EventScroll";
import { EventSummary} from "@/model/event.model";
import Header from "@/components/layout/Header";
import Footer from "@/components/layout/Footer";
import EventHero from "@/components/events/EventHero";

interface HomePageProps {
  popularEvents: EventSummary[];
  trendingEvents: EventSummary[];
}

export default function HomePage(props: HomePageProps) {
  const {popularEvents, trendingEvents} = props;

  return (
    <main className="min-h-screen bg-zinc-50">
      <Header/>
      <div className="container mx-auto px-16 py-2">
        <section className="mb-12 px-20 mx-20">
          <EventHero events={popularEvents}/>
        </section>
        <section className="mb-12">
          <h4 className="text-2xl font-semibold mb-4">Sự kiện nổi bật</h4>
          <EventScroll>
            {popularEvents?.map((event) => (<EventCard key={event.id} event={event}/>))}
          </EventScroll>
        </section>

        <section>
          <h4 className="text-2xl font-semibold mb-4">Sự kiện xu hướng</h4>
          <EventScroll>
            {trendingEvents?.map((event) => (<EventCard key={event.id} event={event}/>))}
          </EventScroll>
        </section>

        <section>
          <h4 className="text-2xl font-semibold mb-4">Dành cho bạn</h4>
          <EventScroll>
            {trendingEvents?.map((event) => (<EventCard key={event.id} event={event}/>))}
          </EventScroll>
        </section>
      </div>
      <Footer/>
    </main>
  );
}
