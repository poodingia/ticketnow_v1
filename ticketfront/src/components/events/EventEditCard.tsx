'use client'

import { updateEvent } from "@/api/event";
import { useForm } from "react-hook-form";
import { EventCreateGeneral, eventCreateSchema } from "@/model/event.model";
import { zodResolver } from "@hookform/resolvers/zod";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Form } from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { toast } from "@/hooks/use-toast";
import { City } from "@/model/city.model";
import CustomFormField from "@/components/form/CusFormField";
import {useState} from "react";

interface EventEditCardProps {
  id: string;
  event: EventCreateGeneral;
  createEvent?: (event: EventCreateGeneral) => void;
  cities: City[];
}

const eventTypes = [
  { key: "CONCERT", label: "Hòa nhạc" },
  { key: "SPORT", label: "Thể thao" },
  { key: "THEATER", label: "Kịch" },
  { key: "MOVIE", label: "Phim" },
  { key: "FESTIVAL", label: "Lễ hội" },
  { key: "CONFERENCE", label: "Hội thảo" },
  { key: "OTHER", label: "Khác" },
];

export default function EventEditCard(props: EventEditCardProps) {
  const { id, event, createEvent, cities } = props;
  const [waiting, setWaiting] = useState(false);

  const eventForm = useForm<EventCreateGeneral>({
    resolver: zodResolver(eventCreateSchema),
    defaultValues: {
      title: event.title || "",
      description: event.description || "",
      category: event.category || "",
      location: event.location || "",
      date: event.date || "",
      startSaleDate: event.startSaleDate || "",
      endSaleDate: event.endSaleDate || "",
      cityId: event.cityId ? event.cityId.toString() : "",
      max: event.max || 0,
      image: event.bgImagePath || "",
    },
  });

  const onSubmit = () => {
    setWaiting(true);
    if (createEvent) {
      createEvent(eventForm.getValues());
    } else {
      updateEvent(id, eventForm.getValues()).then((data) => {
        if (data === "00000") {
          toast({title: "Sự kiện đã được cập nhật",});
        } else {
          toast({title: "Sự kiện cập nhật thất bại",});
        }
      });
    }
    setWaiting(false);
  };

  return (
    <Card className="my-8 mx-auto max-w-4xl">
      <CardHeader>
        {event?.id && <CardTitle>ID: {event?.id}</CardTitle>}
      </CardHeader>
      <CardContent>
        <Form {...eventForm}>
          <form onSubmit={eventForm.handleSubmit(onSubmit)} className="space-y-8">
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
              <CustomFormField<EventCreateGeneral>
                name="title" control={eventForm.control}
                label="Tên sự kiện" placeholder="Nhập tên sự kiện"
              />
              <CustomFormField<EventCreateGeneral>
                name="cityId" control={eventForm.control} label="Tỉnh"
                placeholder="Chọn tỉnh" type="select"
                selects={cities.map((city) => ({ value: city.id.toString(), label: city.name }))}
              />
            </div>
            <CustomFormField<EventCreateGeneral>
              name="description" control={eventForm.control} label="Mô tả"
              placeholder="Nhập mô tả sự kiện" type="editor"
            />
            <CustomFormField<EventCreateGeneral>
              name="location" control={eventForm.control}
              label="Địa điểm" placeholder="Nhập địa điểm tổ chức sự kiện"
            />
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
              <CustomFormField<EventCreateGeneral>
              name="date" control={eventForm.control} label="Ngày tổ chức"
              placeholder="Nhập ngày tổ chức sự kiện" type="datetime-local"
              />
              <CustomFormField<EventCreateGeneral>
              name="category" control={eventForm.control} label="Thể loại"
              placeholder="Chọn thể loại" type="select"
              selects={eventTypes.map((event) => ({ value: event.key, label: event.label }))}
              />
            </div>
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
              <CustomFormField<EventCreateGeneral>
              name="startSaleDate" control={eventForm.control} label="Ngày bắt đầu bán vé"
              placeholder="Nhập ngày bắt đầu bán vé" type="datetime-local"
              />
              <CustomFormField<EventCreateGeneral>
              name="endSaleDate" control={eventForm.control} label="Ngày kết thúc bán vé"
              placeholder="Nhập ngày kết thúc bán vé" type="datetime-local"
              />
            </div>
            <CustomFormField<EventCreateGeneral>
              name="image" control={eventForm.control} label="Hình ảnh sự kiện"
              placeholder="Tải lên một hình ảnh" type="image"
            />
            <div className="flex space-x-4">
              <Button type="submit" disabled={eventForm.formState.isSubmitting}>
                {waiting ? "Đang lưu..." : "Lưu và Tiếp tục"}
              </Button>
            </div>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}