'use client'

import React from "react";
import { Be_Vietnam_Pro } from "next/font/google";
import {useParams} from "next/navigation";
import {useQueueSchedulePosition} from "@/hooks/useFetch";

const beVietnamPro = Be_Vietnam_Pro({
  weight: '400',
  subsets: ['latin'],
})
export default function EmptyLayout({ children }: { children: React.ReactNode }) {
  const params = useParams<{ id: string }>();
  const {isPending: isWaiting, data: queue} = useQueueSchedulePosition(params.id);

  if ( isWaiting || queue !== 0) {
    return (WaitingPage(queue))
  }

  return <div className={`${beVietnamPro.className} min-h-screen bg-zinc-700`}>{children}</div>;
}

function WaitingPage(queue: number | undefined) {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-background to-muted p-4">
      <div className="w-full max-w-md bg-card rounded-xl shadow-lg overflow-hidden">
        <div className="p-8 space-y-6">
          <div className="flex justify-center">
            <div className="relative">
              <div
                className="relative flex items-center justify-center h-32 w-32 rounded-full bg-background border-4 border-muted">
                <div className="text-center">
                  <span className="block text-3xl font-bold">{queue}</span>
                  <span className="text-sm text-muted-foreground">người đang đợi</span>
                </div>
              </div>
            </div>
          </div>

          <div className="space-y-2">
            <h1 className="text-2xl font-bold text-center">Xin hãy đợi </h1>
            <p className="text-center text-muted-foreground">Bạn dang trong hàng chờ</p>
          </div>
        </div>
      </div>
    </div>
  )
}
