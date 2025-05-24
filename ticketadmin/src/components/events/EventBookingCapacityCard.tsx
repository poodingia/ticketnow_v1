"use client"

import type React from "react"
import { useState, useRef } from "react"
import { Button } from "@/components/ui/button"
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {updateBookingCapacity} from "@/api/event";
import {useQueryClient} from "@tanstack/react-query";

export default function EventBookingCapacityModal({id}: {id: string}) {
  const queryClient = useQueryClient();
  const [open, setOpen] = useState(false)
  const [error, setError] = useState("")
  const inputRef = useRef<HTMLInputElement>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    const value = inputRef.current?.value
    const capacityNum = Number.parseInt(value || "")
    if (!value || isNaN(capacityNum) || capacityNum <= 0) {
      setError("Please enter a valid positive number")
      return
    }
    await updateBookingCapacity(id, capacityNum)
    queryClient.invalidateQueries({queryKey: ["event", id]}).then();
    setOpen(false)
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline">Đặt Sức Chứa</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Sức chứa đặt vé</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            {error && <p className="text-sm text-red-500">{error}</p>}
            <div className="flex items-center gap-4">
              <Label htmlFor="capacity" className="w-24">
                Tối đa
              </Label>
              <Input
                id="capacity" type="number" ref={inputRef}
                onChange={() => setError("")} className="flex-1" min="1" defaultValue=""
              />
            </div>
          </div>
          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => setOpen(false)}>
              Hủy
            </Button>
            <Button type="submit">Lưu</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}

