import Link from "next/link"
import { Badge } from "@/components/ui/badge"
import {Avatar, AvatarFallback} from "@/components/ui/avatar";
import {Feedback} from "@/model/feedback.model";

export default function FeedbackList({feedbackItems} : {feedbackItems: Feedback[]}) {
  return (
    <div className="space-y-4 mx-auto ">
      {feedbackItems?.map((item) => (
          <Link href={`/feedback/${item.id}`} key={item.id} className="block transition-all hover:shadow-md">
            <div className="bg-card text-card-foreground rounded-lg shadow p-4">
              <div className="flex items-center justify-between">
                <h3 className="text-lg font-semibold">{item.topic}</h3>
                <Badge
                  variant={item.status === "PENDING" ? "secondary" : item.status === "RESOLVED" ? "default" : "outline"}>
                  {item.status === "RESOLVED" ? "Đã xong" : "Chờ xử lý"}
                </Badge>
              </div>
              <div className="mt-2 flex items-center">
                <Avatar className="h-6 w-6 mr-2">
                  <AvatarFallback>{item.username[0]}</AvatarFallback>
                </Avatar>
              </div>
            </div>
          </Link>
      ))}
    </div>
  )
}


