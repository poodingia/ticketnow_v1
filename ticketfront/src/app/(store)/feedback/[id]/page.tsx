"use client"

import { useState } from "react"
import { useParams } from "next/navigation"
import Link from "next/link"
import { useFeedbackDetail, useFeedbackReplies } from "@/hooks/useFetch"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Skeleton } from "@/components/ui/skeleton"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Separator } from "@/components/ui/separator"
import { toast } from "@/hooks/use-toast"
import {postFeedbackReply} from "@/api/feedback";
import ResolveButton from "@/components/feedback/FeedbackButton";
import TiptapEditor from "@/components/editor/TipTapEditor";
import {Avatar, AvatarFallback} from "@/components/ui/avatar";

export default function Home() {
  const params = useParams<{ id: string }>()
  const { data: feedback, isPending: isFeedbackPending, refetch: refetchFeedback } = useFeedbackDetail(params.id)
  const { data: feedbackReplies, isPending: isRepliesPending, refetch: refetchReplies } = useFeedbackReplies(params.id)
  const [replyMessage, setReplyMessage] = useState("")
  const [isSubmitting, setIsSubmitting] = useState(false)

  const handleReplySubmit = async () => {
    if (!replyMessage.trim()) return
    setIsSubmitting(true)
    try {
      await postFeedbackReply(params.id, replyMessage)
      toast({
        title: "Reply Sent",
        description: "Your reply has been successfully sent.",
      })
      setReplyMessage("")
      await Promise.all([refetchReplies(), refetchFeedback()])
    } catch {
      toast({
        title: "Error",
        description: "Failed to send reply. Please try again.",
        variant: "destructive",
      })
    } finally {
      setIsSubmitting(false)
    }
  }

  if (isFeedbackPending || isRepliesPending) {
    return <FeedbackDetailSkeleton />
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8">
        <Link href="/feedback">
          <Button variant="outline">Quay lại</Button>
        </Link>
      </div>
      <div className="max-w-2xl mx-auto">
        <div className="flex justify-between items-center">
          <CardTitle className="text-xl font-bold">{feedback?.topic}</CardTitle>
          <Badge variant={feedback?.status === "RESOLVED" ? "default" : "secondary"}>
            {feedback?.status  === "RESOLVED" ? "Đã xong" : "Chờ xử lý"}
          </Badge>
        </div>
        <div>
          {feedback?.message && <div dangerouslySetInnerHTML={{__html: feedback.message}}/>}
          <Separator className="my-4"/>
          <h3 className="text-lg font-semibold mb-2">Phản hồi</h3>
          <ScrollArea className="h-[200px] rounded-md border p-4">
            {feedbackReplies && feedbackReplies.length > 0 ? (
              feedbackReplies.map((reply) => (
                <div key={reply.id} className="flex space-x-3 mb-4">
                  <Avatar>
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
              ))
            ) : (
              <p className="text-center text-muted-foreground">Chưa có phản hồi.</p>
            )}
          </ScrollArea>
        </div>
      </div>
      <div className="max-w-2xl mx-auto pt-3">
        <h3 className="text-lg font-semibold">Thêm phản hồi</h3>
        <TiptapEditor value={replyMessage} onChange={setReplyMessage}/>
        <ResolveButton id={params.id}/>
        <Button onClick={handleReplySubmit} disabled={!replyMessage.trim() || isSubmitting} className="ml-5">
          {isSubmitting ? "Đang gửi..." : "Gửi"}
        </Button>
      </div>
    </div>
  )
}

function FeedbackDetailSkeleton() {
  return (
    <Card className="max-w-2xl mx-auto">
      <CardHeader className="flex flex-row items-center justify-between space-y-0">
        <Skeleton className="h-8 w-[250px]"/>
        <Skeleton className="h-6 w-[100px]"/>
      </CardHeader>
      <CardContent className="space-y-4">
        <Skeleton className="h-4 w-full"/>
        <Skeleton className="h-4 w-full"/>
        <Skeleton className="h-4 w-[90%]" />
        <Skeleton className="h-4 w-[85%]" />
        <Skeleton className="h-4 w-[80%]" />
      </CardContent>
      <CardFooter className="flex justify-end">
        <Skeleton className="h-10 w-[100px]" />
      </CardFooter>
    </Card>
  )
}

