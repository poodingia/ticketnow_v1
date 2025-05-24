import {API_CRUD_URL} from "@/utils/constant";
import {TicketType, TicketTypeCreate, TicketTypeListCreate} from "@/model/ticketType.model";
import axios from "axios";

const api = axios.create({
  baseURL: API_CRUD_URL,
  headers: { "Content-Type": "application/json" }
});

export const fetchTicketTypes = async (eventId: string): Promise<TicketType[]> => {
  const { data } = await api.get(`/event/${eventId}/ticket-type`);
  return data.data;
};

export const updateTicketTypes = async (eventId: string, ticketTypes: TicketTypeListCreate) => {
  const ticketTypeData = ticketTypes.ticketTypes.map((ticket: TicketTypeCreate) => (
    { ...ticket, eventId: Number(eventId) }
  ));
  const { data } = await api.put(`/ticket-type/list`, ticketTypeData);
  return data.data;
};

