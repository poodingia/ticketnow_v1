'use client'

import Link from "next/link";
import {Button} from "@/components/ui/button";
import FeedbackList from "@/components/feedback/FeedbackList";
import {Suspense} from "react";
import {useFeedbacks} from "@/hooks/useFetch";

export default function Home() {
  const {data: feedbacks} = useFeedbacks();
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Khiếu nại</h1>
        <Link href="/feedback/create">
          <Button>Tạo mới khiếu nại</Button>
        </Link>
      </div>
      <Suspense>
        {feedbacks &&  <FeedbackList feedbackItems={feedbacks}/>}
      </Suspense>
    </div>
)
}