import {EventSummary} from "@/model/event.model";
import Link from "next/link";
import {generateSlug} from "@/utils/utils";
import Image from "next/image";
import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel";
import Autoplay from "embla-carousel-autoplay"

export default function EventHero({events}: { events: EventSummary[] }) {
  return (
    <Carousel className="w-full" plugins={[Autoplay({delay: 2000,}),]} >
      <CarouselContent>
        {events.map((event, index) => (
          <CarouselItem key={index}>
            <Link href={`/events/${generateSlug(event.title)}-${String(event.id).slice(-1)}`} className="block w-[1100px]">
              <div className={`overflow-hidden group h-[480px] transition-all duration-300 mb-6`}>
                <div className="relative w-full h-full">
                  <Image
                    src={event.bgImagePath || "/placeholder.svg"} alt={event.title}
                    fill style={{objectFit: "cover"}}
                    className="rounded-xl" priority
                  />
                </div>
              </div>
            </Link>
          </CarouselItem>
        ))}
      </CarouselContent>
      <CarouselPrevious />
      <CarouselNext />
    </Carousel>

  )


}