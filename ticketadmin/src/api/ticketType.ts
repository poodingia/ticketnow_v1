import {API_CRUD_URL} from "@/utils/constant";
import {TicketType} from "@/model/ticketType.model";

export const fetchTicketTypes = async (eventId: string) : Promise<TicketType[]>=> {
  const response = await fetch(`${API_CRUD_URL}/event/${eventId}/ticket-type`);
  if (!response.ok) {
    throw new Error(`Failed to fetch ticket types`);
  }
  const data = await response.json();

  return data.data;
};