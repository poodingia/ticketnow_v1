"use client"

import type React from "react"
import {useRef} from "react"
import {Button} from "@/components/ui/button"
import {ChevronLeft, ChevronRight} from "lucide-react"
import {ScrollArea, ScrollBar} from "@/components/ui/scroll-area"

export default function EventScroll({children}: { children: React.ReactNode }) {
  const scrollAreaRef = useRef<HTMLDivElement>(null)

  const scroll = (direction: "left" | "right") => {
    if (scrollAreaRef.current) {
      const scrollContainer = scrollAreaRef.current.querySelector("div[data-radix-scroll-area-viewport]",) as HTMLDivElement
      if (scrollContainer) {
        const scrollAmount = scrollContainer.clientWidth / 2
        scrollContainer.scrollBy({
          left: direction === "left" ? -scrollAmount : scrollAmount, behavior: "smooth",
        })
      }
    }
  }

  return (<div className="space-y-4">
      <div className="relative">
        <ScrollArea ref={scrollAreaRef} className="w-full pb-4">
          <div className="flex space-x-4 px-1">{children}</div>
          <ScrollBar orientation="horizontal" className="h-1.5"/>
        </ScrollArea>
        <Button
          variant="outline" size="icon" onClick={() => scroll("left")}
          className="absolute left-0 top-1/4 -translate-y-1/2 h-9 w-9 rounded-full bg-background/80 backdrop-blur-sm shadow-sm border-muted z-10 opacity-80 hover:opacity-100"
        >
          <ChevronLeft className="h-5 w-5"/>
          <span className="sr-only">Scroll left</span>
        </Button>
        <Button
          variant="outline" size="icon" onClick={() => scroll("right")}
          className="absolute right-0 top-1/4 -translate-y-1/2 h-9 w-9 rounded-full bg-background/80 backdrop-blur-sm shadow-sm border-muted z-10 opacity-80 hover:opacity-100"
        >
          <ChevronRight className="h-5 w-5"/>
          <span className="sr-only">Scroll right</span>
        </Button>
      </div>
    </div>)
}

