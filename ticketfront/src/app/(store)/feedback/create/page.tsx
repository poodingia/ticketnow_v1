'use client'

import { Button } from "@/components/ui/button"
import Link from "next/link"
import {FeedbackCreate, FeedbackCreateSchema} from "@/model/feedback.model";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form} from "@/components/ui/form";
import {useMutation} from "@tanstack/react-query";
import {toast} from "@/hooks/use-toast";
import {createFeedback} from "@/api/feedback";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {ArrowLeft, Send} from "lucide-react";
import CustomFormField from "@/components/form/CusFormField";
import {useRouter} from "next/navigation";

export default function Home() {
  const router = useRouter()
  const form = useForm<FeedbackCreate>({
    resolver: zodResolver(FeedbackCreateSchema),
    defaultValues: {
      topic: "",
      message: "",
    }
  });

  const mutation = useMutation({
    mutationFn: createFeedback,
    onSuccess: () => {
      toast({
        title: "Feedback Created",
        description: "Feedback has been successfully created.",
      })
      router.push("/feedback")
    },
  });

  const onSubmit = (data: FeedbackCreate) => {
    mutation.mutate(data);
  };

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="text-2xl font-bold">Tạo mới khiếu nại</CardTitle>
            <Link href="/feedback">
              <Button variant="ghost" size="sm">
                <ArrowLeft className="mr-2 h-4 w-4"/>
                Trở về danh sách
              </Button>
            </Link>
          </div>
          <CardDescription>Chia sẻ cảm nghĩ của bạn và giúp chúng tôi cải thiện.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <CustomFormField<FeedbackCreate>
                name="topic" control={form.control} label="Chủ đề"
                placeholder="Chủ đề khiếu nại" type="text"
              />
              <CustomFormField<FeedbackCreate>
                name="message" control={form.control} label="Mô tả"
                placeholder="Mô tả chi tiết" type="editor"
              />
              <Button type="submit" className="w-full" >
                  <><Send className="mr-2 h-4 w-4"/>Gửi khiếu nại</>
              </Button>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  )
}

