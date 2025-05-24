"use client"

import {useState} from "react"
import {useRouter} from "next/navigation";
import {Button} from "@/components/ui/button"
import {CardContent, CardFooter, CardTitle} from "@/components/ui/card"
import EventGeneralForm from "@/components/events/create/EventGeneralForm";
import EventTicketTypesForm from "@/components/events/create/EventTicketTypesForm";
import Confirmation from "@/components/events/create/Confirmation";
import {createEvent} from "@/api/event";
import {EventCreateForm, EventCreateGeneral} from "@/model/event.model";
import {TicketTypeListCreate} from "@/model/ticketType.model";
import {useMutation} from "@tanstack/react-query";
import {Loader2} from "lucide-react";

const steps = [
  { title: "General Information", component: EventGeneralForm, name: 'event' },
  { title: "Ticket Types", component: EventTicketTypesForm, name: 'eventTicketTypes' },
  { title: "Confirmation", component: Confirmation, name: 'confirmation' },
]

export default function CreateEvent() {
  const router = useRouter();
  const [currentStep, setCurrentStep] = useState(0)
  const [formData, setFormData] = useState<EventCreateForm>({
    event: {
      title: "",
      description: "",
      category: "",
      location: "",
      date: "",
      startSaleDate: "",
      endSaleDate: "",
      cityId: ""
    },
    eventTicketTypes: {
      ticketTypes: []
    },
  })

  const CurrentStepComponent = steps[currentStep].component
  const mutation = useMutation({
    mutationFn: createEvent,
    onSuccess: () => {
      router.push('/')
    },
  })

  const handleNext = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(currentStep + 1)
    }
  }

  const handlePrevious = () => {
    if (currentStep > 0) {
      setCurrentStep(currentStep - 1)
    }
  }

  const updateFormData = (stepData: EventCreateGeneral | TicketTypeListCreate) => {
    setFormData((prevData) => ({
      ...prevData,
      [steps[currentStep].name]: stepData,
    }))
    handleNext()
  }

  const handleSubmit = () => {
    mutation.mutate(formData)
  }

  return (
    <div className="container mx-auto py-10">
      {mutation.isPending && (
        <div className="absolute inset-0 bg-background/80 backdrop-blur-sm flex flex-col items-center justify-center z-50">
          <Loader2 className="h-12 w-12 animate-spin text-primary mb-4" />
          <p className="text-lg font-medium">Đang tạo sự kiện...</p>
          <p className="text-sm text-muted-foreground mt-2">Vui lòng đợi trong giây lát</p>
        </div>
      )}

      <div className="w-full max-w-4xl mx-auto">
        <div>
          <CardTitle>Tạo sự kiện - Bước {currentStep + 1}</CardTitle>
        </div>
        <CardContent>
          <CurrentStepComponent formData={formData} updateFormData={updateFormData}></CurrentStepComponent>
        </CardContent>
        <CardFooter className="flex justify-between">
          <Button onClick={handlePrevious} disabled={currentStep === 0}>
            Quay lại
          </Button>
          {currentStep === steps.length - 1 && !mutation.isPending && <Button onClick={handleSubmit}>Tạo sự kiện</Button>}
        </CardFooter>
      </div>
    </div>
  )
}


