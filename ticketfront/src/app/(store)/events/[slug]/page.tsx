import Image from "next/image";
import Link from "next/link";
import {CalendarDays, MapPin} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Card, CardContent} from "@/components/ui/card";
import {fetchEventDetail} from "@/api/event";
import {Metadata} from "next";
import {Separator} from "@/components/ui/separator";
import {fetchTicketTypes} from "@/api/ticketType";
import {formatCurrency, formatDate} from "@/utils/utils";
import EventSimilar from "@/components/events/EventSimilar";
import {Suspense} from "react";
import FollowButton from "@/components/events/FollowButton";

export async function generateMetadata({params}: { params: { slug: string } }): Promise<Metadata> {
  const slugParts = params.slug.split("-");
  const eventId = slugParts.length > 1 ? slugParts[slugParts.length - 1] : params.slug;
  const event = await fetchEventDetail(eventId);
  return {
    title: event ? `${event.title} - TicketNow` : "Không tìm thấy sự kiện",
    description: event?.description || "Hãy xem sự kiện tuyệt vời này!",
    openGraph: {
      title: event?.title, description: event?.description, images: [event?.bgImagePath],
    },
  };
}

export default async function EventPage({params}: { params: { slug: string } }) {
  const lastHyphenIndex = params.slug.lastIndexOf("-");
  const eventId = params.slug.substring(lastHyphenIndex + 1);
  const event = await fetchEventDetail(eventId);
  const ticketTypes = await fetchTicketTypes(eventId);

  const eventDate = formatDate(event.date);
  const saleStartDate = formatDate(event.startSaleDate);
  const saleEndDate = formatDate(event.endSaleDate);
  const today = new Date()
  const startSale = new Date(event.startSaleDate)
  const endSale = new Date(event.endSaleDate)
  const isOnSale = today >= startSale && today <= endSale
  const isSoldOut = ticketTypes?.every((ticketType) => ticketType.quantity === ticketType.reservedQuantity)

  return (<div className="min-h-screen bg-background">
    <div className="relative h-[40vh] md:h-[50vh] w-full">
      <Image
        src={event.bgImagePath || "/placeholder.svg"}
        alt={event.title}
        fill
        className="object-cover brightness-75"
        priority
      />
      <div className="absolute inset-0 bg-gradient-to-t from-background to-transparent"/>
      <div className="absolute bottom-0 left-0 w-full p-6 md:p-10">
        <h1 className="text-3xl md:text-5xl font-bold text-white mb-2">{event.title}</h1>
        <div className="flex items-center text-white/90 gap-2">
          <CalendarDays className="h-5 w-5"/>
          <span>{eventDate}</span>
        </div>
      </div>
    </div>
    <div className="container mx-auto px-4 py-8 md:py-12">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="md:col-span-2 space-y-6">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-2xl font-semibold">Về Sự Kiện Này</h2>
                <FollowButton id={eventId}/>
              </div>
              <div
                className="text-muted-foreground"
                dangerouslySetInnerHTML={{__html: event.description}}
              />
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6 space-y-4">
              <h2 className="text-2xl font-semibold mb-2">Chi Tiết Sự Kiện</h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="flex items-center gap-3">
                  <div className="bg-primary/10 p-2 rounded-full">
                    <CalendarDays className="h-5 w-5 text-primary"/>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Thời gian</p>
                    <p className="font-medium">{eventDate}</p>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  <div className="bg-primary/10 p-2 rounded-full">
                    <MapPin className="h-5 w-5 text-primary"/>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Địa Điểm</p>
                    <p className="font-medium">{event.location}</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        <div>
          <Card className="sticky top-8">
            <CardContent className="p-6 space-y-6">
              <h2 className="text-2xl font-semibold">Hạng Vé</h2>
              <div className="space-y-4">
                <div>
                  <p className="text-sm text-muted-foreground">Thời Gian Bán Vé</p>
                  <p className="font-medium">
                    {saleStartDate} - {saleEndDate}
                  </p>
                </div>
                <Separator/>
                {ticketTypes?.map((ticketType) => (<div key={ticketType.id} className="space-y-2">
                  <div className="flex justify-between items-center">
                    <span className="font-medium">{ticketType.description}</span>
                    <span className="font-bold">{formatCurrency(parseFloat(ticketType.price))}</span>
                  </div>
                  <p className="text-sm text-muted-foreground">
                    {isOnSale && `${ticketType.quantity - ticketType.reservedQuantity} vé còn lại`}
                  </p>
                </div>))}

                <Separator/>

              {isOnSale && !isSoldOut ? (
              <Link href={`/booking/${eventId}`}>
                <Button className="w-full" size="lg">Mua Vé</Button>
              </Link>) :
              (<Button className="w-full" size="lg" disabled>
                {isSoldOut ? "Đã bán hết vé" : (today < startSale ? "Vé chưa được bán" : "Đã kết thúc bán vé")}
              </Button>)
              }

                <p className="text-xs text-center text-muted-foreground">Thanh toán an toàn • Xác nhận ngay lập tức</p>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
    <Separator/>
    <div className="bg-zinc-100">
      <div className="container mx-auto px-4 py-4 ">
        <h5 className="text-xl font-semibold mb-4 text-center">Sự Kiện Bạn Có Thể Quan Tâm</h5>
        <Suspense><EventSimilar title={event.title}/></Suspense>
      </div>
    </div>
  </div>)
}