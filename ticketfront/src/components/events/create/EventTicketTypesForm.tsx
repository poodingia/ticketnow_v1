"use client"
import {useFieldArray, useForm} from "react-hook-form"
import {zodResolver} from "@hookform/resolvers/zod"
import {Button} from "@/components/ui/button"
import {Form} from "@/components/ui/form"
import {Trash2} from "lucide-react"
import {TicketTypeListCreate, ticketTypeListCreateSchema} from "@/model/ticketType.model";
import {EventCreateStepProps} from "@/model/event.model";
import CustomFormField from "@/components/form/CusFormField";
import { Card } from "@/components/ui/card"
import {useState} from "react";

export default function EventTicketTypesForm({ formData, updateFormData }: EventCreateStepProps) {
  const form = useForm<TicketTypeListCreate>({
    resolver: zodResolver(ticketTypeListCreateSchema),
    defaultValues: {
      ticketTypes: formData.eventTicketTypes?.ticketTypes?.length ? formData.eventTicketTypes.ticketTypes : [{ description: "", price: 0, quantity: 0 }],
    },
  })

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "ticketTypes",
  })

  const [waiting, setWaiting] = useState(false);


  function onSubmit(data: TicketTypeListCreate) {
    setWaiting(true)
    updateFormData(data)
    setWaiting(false)
  }
  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        {fields.map((field, index) => (
          <Card key={field.id} className="mx-auto mt-4 p-5">
            <div key={field.id} className="space-y-4">
              <h3 className="text-lg font-semibold">Hạng vé {index + 1}</h3>
              <CustomFormField<TicketTypeListCreate>
                name={`ticketTypes.${index}.description`} control={form.control}
                label="Tên hạng vé" placeholder="Nhập tên hạng vé" type="text"
              />
              <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                <CustomFormField<TicketTypeListCreate>
                  name={`ticketTypes.${index}.price`} control={form.control}
                  label="Giá" placeholder="Nhập giá hạng vé" type="number"
                />
                <CustomFormField<TicketTypeListCreate>
                  name={`ticketTypes.${index}.quantity`} control={form.control}
                  label="Số lượng" placeholder="Nhập số lượng vé" type="number"
                />
              </div>
              <Button type="button" variant="destructive" size="sm" onClick={() => remove(index)}>
                <Trash2 className="mr-2 h-4 w-4"/>
                Xóa hạng vé
              </Button>
            </div>
          </Card>
        ))}
        <Button type="button" variant="outline" onClick={() => append({id: 0, description: "", price: 0, quantity: 0})} className="mr-5">
          Thêm hạng vé
        </Button>
        <Button type="submit" disabled={form.formState.isSubmitting}>
          {waiting ? "Đang lưu..." : "Lưu và Tiếp tục"}
        </Button>
      </form>
    </Form>
  )
}

