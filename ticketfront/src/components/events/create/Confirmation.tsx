import Image from "next/image"
import {EventCreateStepProps} from "@/model/event.model";
import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {Separator} from "@radix-ui/react-dropdown-menu";
import {MapPinIcon, TagIcon, TicketIcon} from "lucide-react";
import {formatCurrency} from "@/utils/utils";

export default function Confirmation({formData}: EventCreateStepProps) {
  return (
    <Card className="w-full overflow-hidden mt-4">
      <CardHeader className="bg-primary/5 pb-0">
        <div className="flex flex-col gap-2">
          <h2 className="text-2xl font-bold">{formData.event.title}</h2>
        </div>
      </CardHeader>

      <CardContent className="p-6 space-y-6">
        {formData.event.image && (<div className="relative w-full h-64 md:h-80">
          <Image
            src={formData.event.image || "/placeholder.svg"}
            alt={`${formData.event.title} event image`}
            fill
            className="object-cover"
            priority
          />
        </div>)}

        <div className="space-y-4">
          <div className="space-y-2">
            <h3 className="text-lg font-semibold flex items-center gap-2">
              <TagIcon className="h-5 w-5 text-muted-foreground"/>
              Thông tin sự kiện
            </h3>
            <div
              className="text-muted-foreground"
              dangerouslySetInnerHTML={{__html: formData.event.description}}
            />
          </div>

          <div className="flex items-center gap-2">
          <MapPinIcon className="h-5 w-5 text-muted-foreground"/>
            <span>{formData.event.location}</span>
          </div>
        </div>

        <Separator/>

        <div className="space-y-4">
          <h3 className="text-lg font-semibold flex items-center gap-2">
            <TicketIcon className="h-5 w-5 text-muted-foreground"/>
            Thông tin vé
          </h3>
          <div className="grid gap-3">
            {formData.eventTicketTypes.ticketTypes.map((ticket, index) => (
              <div key={index} className="flex justify-between items-center p-3 rounded-lg border bg-card">
                <div>
                  <p className="font-medium">{ticket.description}</p>
                  <p className="text-sm text-muted-foreground">{ticket.quantity} chỗ</p>
                </div>
                <div className="text-right">
                  <p className="text-lg font-bold">{formatCurrency(ticket.price)}</p>
                </div>
              </div>))}
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

