'use client'

import {useMutation} from "@tanstack/react-query";
import {updateTicketTypes} from "@/api/ticketType";
import {useFieldArray, useForm} from "react-hook-form";
import {TicketType, TicketTypeListCreate, ticketTypeListCreateSchema} from "@/model/ticketType.model";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Form} from "@/components/ui/form";
import {Button} from "@/components/ui/button";
import {Trash2} from "lucide-react";
import {toast} from "@/hooks/use-toast"
import CustomFormField from "@/components/form/CusFormField";
import {zodResolver} from "@hookform/resolvers/zod";

interface TicketTypeListCardProps {
  id: string;
  ticketTypes: TicketType[]
}

export default function TicketTypeListCard(props: TicketTypeListCardProps) {
  const {id, ticketTypes} = props;

  const mutation = useMutation({
    mutationFn: (data: { eventId: string, ticketTypes: TicketTypeListCreate }) =>
      updateTicketTypes(data.eventId, data.ticketTypes),
    onSuccess: () => {
      toast({
        title: "Hạng vé đã được cập nhật",
      });
    },
  });

  const ticketTypeForm = useForm<TicketTypeListCreate>({
    resolver: zodResolver(ticketTypeListCreateSchema),
    defaultValues: {
      ticketTypes: ticketTypes || [],
    }
  });

  const {fields, append, remove} = useFieldArray({
    control: ticketTypeForm.control,
    name: "ticketTypes",
  });


  const onSubmit = () => {
    mutation.mutate({eventId: id, ticketTypes: ticketTypeForm.getValues()});
  };

  return (
    <Card className="my-8 mx-auto max-w-4xl mb-4">
      <CardHeader>
        <CardTitle>Hạng vé</CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...ticketTypeForm}>
          <form onSubmit={ticketTypeForm.handleSubmit(onSubmit)} className="space-y-8">
            {fields.map((field, index) => (
              <div key={field.id} className="space-y-4">
                <h3 className="text-lg font-semibold">Hạng vé {index + 1}</h3>
                <CustomFormField<TicketTypeListCreate>
                  name={`ticketTypes.${index}.description`} control={ticketTypeForm.control}
                  label="Ticket Name" placeholder="Nhập tên" type="text"
                />
                <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
                  <CustomFormField<TicketTypeListCreate>
                    name={`ticketTypes.${index}.price`} control={ticketTypeForm.control}
                    label="Price" placeholder="Nhập giá" type="number"
                  />
                  <CustomFormField<TicketTypeListCreate>
                    name={`ticketTypes.${index}.quantity`} control={ticketTypeForm.control}
                    label="Quantity" placeholder="Nhập số lượng" type="number"
                  />
                </div>
                <Button type="button" variant="destructive" size="sm" onClick={() => remove(index)}>
                  <Trash2 className="mr-2 h-4 w-4"/>
                  Xóa hạng vé
                </Button>
              </div>
            ))}
            <Button type="button" variant="outline" className={"mr-5"}
                    onClick={() => append({id: 0, description: "", price: 0, quantity: 0})}>
              Thêm hạng vé
            </Button>
            <Button type="submit">Lưu và tiếp tục</Button>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}