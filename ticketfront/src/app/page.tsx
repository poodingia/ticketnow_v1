import HomePage from "@/components/pages/HomePage"
import {fetchEvents} from "@/api/event";

export default async function Home() {
  const popularEvents = await fetchEvents();
  const trendingEvents = await fetchEvents();
  return (
    <HomePage popularEvents={popularEvents} trendingEvents={trendingEvents} />
  )
}