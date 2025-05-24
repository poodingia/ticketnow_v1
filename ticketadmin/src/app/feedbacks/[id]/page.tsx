"use client"

import { useParams } from "next/navigation"
import { useFeedbackDetail, useFeedbackReplies } from "@/hooks/useFetch"
import { AlertCircle, ArrowLeft, Send} from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import Link from "next/link"
import { useState } from "react"
import { postFeedbackReply } from "@/api/feedback"
import { toast } from "@/hooks/use-toast"
import type { FeedbackReply } from "@/model/feedback.model"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import TiptapEditor from "@/components/editor/TipTapEditor"
import {Badge} from "@/components/ui/badge";

const ReplyItem = ({ reply }: { reply: FeedbackReply }) => (
  <div className="flex space-x-3 mb-4">
    <Avatar>
      <AvatarImage src="/placeholder-avatar.jpg" alt="Avatar" />
      <AvatarFallback>{reply.username.charAt(0).toUpperCase()}</AvatarFallback>
    </Avatar>
    <div className="flex-1">
      <p className="text-sm font-medium mb-1">{reply.username}</p>
      <div className="bg-muted p-3 rounded-lg">
        <div dangerouslySetInnerHTML={{__html: reply.message}} className="text-sm"/>
      </div>
      <p className="text-xs text-muted-foreground mt-1">{new Date(reply.createdAt).toLocaleString()}</p>
    </div>
  </div>
)

export default function Home() {
  const params = useParams<{ id: string }>()
  const { data: feedback, refetch: refetchFeedback } = useFeedbackDetail(params.id)
  const { data: feedbackReplies, refetch: refetchReplies } = useFeedbackReplies(params.id)
  const [replyMessage, setReplyMessage] = useState<string>("")
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleReplySubmit = async () => {
    if (!replyMessage.trim()) return
    setIsSubmitting(true)
    try {
      await postFeedbackReply(params.id, replyMessage)
      toast({
        title: "Đã gửi",
        description: "Tin nhắn của bạn dã đã được gửi.",
      })
      setReplyMessage("")
      await Promise.all([refetchReplies(), refetchFeedback()])
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-3xl font-bold">Khiếu nại</h1>
        <Link href="/feedbacks">
          <Button variant="outline" size="sm">
            <ArrowLeft className="mr-2 h-4 w-4" />
            Quay lại
          </Button>
        </Link>
      </div>

      <div className="grid gap-6 md:grid-cols-3">
        <Card className="md:col-span-3 shadow-md">
          <CardHeader className="flex flex-row items-start justify-between pb-2">
            <div>
              <CardTitle className="text-xl font-bold">{feedback?.topic}</CardTitle>
              <p className="text-sm text-muted-foreground">
                Được gửi vào {feedback?.createdAt ? new Date(feedback.createdAt).toLocaleString() : ""} bởi {feedback?.username}
              </p>
            </div>
            <Badge>{feedback?.status  === "RESOLVED" ? "Đã xong" : "Chờ xử lý"}</Badge>
          </CardHeader>
          <CardContent>
            <div className=" p-4 rounded-lg border border-muted">
              {feedback?.message && <div dangerouslySetInnerHTML={{__html: feedback.message}}/>}
            </div>
          </CardContent>
        </Card>

        <div className="md:col-span-3">
          <Card className="shadow-md">
            <CardHeader className="pb-2">
              <CardTitle className="text-xl font-bold">Trả lời</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="border rounded-md">
                  <TiptapEditor value={replyMessage} onChange={setReplyMessage} />
                </div>
                <Button
                  onClick={handleReplySubmit}
                  disabled={!replyMessage.trim() || isSubmitting}
                  className="w-full md:w-auto md:ml-auto md:flex"
                >
                  {isSubmitting ? (
                    <span className="flex items-center justify-center">
                      <span className="animate-spin mr-2 h-4 w-4 border-2 border-current border-t-transparent rounded-full"></span>
                      Đang gửi...
                    </span>
                  ) : (
                    <span className="flex items-center justify-center">
                      <Send className="mr-2 h-4 w-4" />
                      Gửi
                    </span>
                  )}
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="md:col-span-3">
          <Card className="shadow-md">
            <CardHeader className="pb-2">
              <CardTitle className="text-xl font-bold">Phản hồi</CardTitle>
            </CardHeader>
            <CardContent className="p-4">
              <ScrollArea className="h-[400px] pr-4">
                {feedbackReplies && feedbackReplies.length > 0 ? (
                  feedbackReplies.map((reply: FeedbackReply) => <ReplyItem key={reply.id} reply={reply} />)
                ) : (
                  <div className="flex flex-col items-center justify-center h-full text-center p-8">
                    <AlertCircle className="h-12 w-12 text-muted-foreground mb-2 opacity-50" />
                    <p className="text-muted-foreground">Chửa có phản hồi.</p>
                    <p className="text-xs text-muted-foreground mt-1">Hãy là người phản hồi đầu tiên.</p>
                  </div>
                )}
              </ScrollArea>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  )
}

