"use client"

import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {useMutation} from "@tanstack/react-query"
import {updateCustomerProfile} from "@/api/account"

import {Button} from "@/components/ui/button"
import {Form} from "@/components/ui/form"
import {Card, CardContent, CardFooter, CardHeader, CardTitle} from "@/components/ui/card"
import {toast} from "@/hooks/use-toast"
import {CustomerProfile, formSchema} from "@/model/account.model";
import CustomFormField from "@/components/form/CusFormField";

export default function ProfileForm({ customer }: { customer: CustomerProfile }) {
  const form = useForm<CustomerProfile>({
    resolver: zodResolver(formSchema),
    defaultValues: customer,
  })

  const mutation = useMutation({
    mutationFn: updateCustomerProfile,
    onSuccess: () => {
        toast({
          title: "Đã cập nhật hồ sơ",
        })
    },
    onError: () => {
      toast({
        title: "Lỗi khi cập nhât",
        variant: "destructive",
      })
    },
  })

  function onSubmit(values: CustomerProfile) {
    mutation.mutate(values)
  }

  return (
    <Card className="w-full max-w-2xl mx-auto">
      <CardHeader>
        <CardTitle>Thông tin tài khoản</CardTitle>
      </CardHeader>
      <CardContent>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <CustomFormField<CustomerProfile>
              control={form.control} name="email" label="Email" placeholder="Nhập email" type="email"
            />
            <CustomFormField<CustomerProfile>
              control={form.control} name="firstName" label="Tên" placeholder="Nhập tên của bạn"
            />
            <CustomFormField<CustomerProfile>
              control={form.control} name="lastName" label="Họ" placeholder="Nhập họ của bạn"
            />
          </form>
        </Form>
      </CardContent>
      <CardFooter>
        <Button type="submit" onClick={form.handleSubmit(onSubmit)} disabled={mutation.isPending}>
          {mutation.isPending ? "Đang lưu..." : "Lưu"}
        </Button>
      </CardFooter>
    </Card>
  )
}

