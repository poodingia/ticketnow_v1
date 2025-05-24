'use client'

import {EventCreateGeneral, EventCreateStepProps} from "@/model/event.model";
import EventEditCard from "@/components/events/EventEditCard";
import {useCities} from "@/hooks/useFetch";

export default function EventGeneralForm({formData, updateFormData}: EventCreateStepProps) {
  const {data: cities} = useCities();
  function onSubmit(values: EventCreateGeneral) {
    const updatedValues = {...values}
    updateFormData(updatedValues)
  }

  return (
      cities && <EventEditCard event={formData.event} createEvent={onSubmit} id={""} cities={cities}/>
  )
}