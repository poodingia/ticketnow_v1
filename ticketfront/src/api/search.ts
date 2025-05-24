import {API_SEARCH_URL} from "@/utils/constant";
import {EventSummary} from "@/model/event.model";

export const searchEvents = async (title: string, category: string): Promise<EventSummary[]> => {
  const response = await fetch(`${API_SEARCH_URL}/event?title=${title}&category=${category.toUpperCase()}`,{ cache: 'no-store' });
  return await response.json();
};

export const searchSimilarEvents = async (title: string): Promise<EventSummary[]> => {
  const response = await fetch(`${API_SEARCH_URL}/event/similar?title=${title}`, { cache: 'no-store' });
  return await response.json();
}