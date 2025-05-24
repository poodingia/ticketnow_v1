import React, {useState} from "react";
import {Coupon, CreateCoupon} from "@/model/event.model";
import {Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import {Pen, Plus} from "lucide-react";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {createCoupon} from "@/api/event";

interface CouponModalProps {
  open: boolean;
  setOpen: (open: boolean) => void;
  id: string;
  coupon?: Coupon;
}

export default function CouponModal({open, setOpen, id, coupon}: CouponModalProps) {
  const [newCoupon, setNewCoupon] = useState<CreateCoupon>({
    eventId: Number(id),
    code: coupon?.code || "",
    value: coupon?.value || 0,
    category: coupon?.category || "FIXED",
    quantity: coupon?.quantity || 0
  })

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    const changeCoupon: CreateCoupon = {
      eventId: Number(id),
      code: newCoupon.code,
      value: Number(newCoupon.value),
      category: newCoupon.category,
      quantity: newCoupon.quantity,
      id: coupon?.id
    }
    await createCoupon(changeCoupon);
    window.location.reload();
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button>
          {coupon ? <Pen/> : <Plus className="w-4 h-4 mr-2"/>}
          {coupon ? "Sửa " : "Thêm"}
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{coupon ? "Chỉnh sửa mã giảm giá": "Thêm mã giảm giá" } </DialogTitle>
          <DialogDescription>{coupon ? "Chỉnh sửa mã giảm giá cho sự kiện của bạn": "Thêm mã giảm giá cho sự kiện của bạn" }</DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="code">Mã giảm giá</Label>
            <Input
              id="code" placeholder="e.g. SUMMER2024" value={newCoupon.code}
              onChange={(e) => setNewCoupon({...newCoupon, code: e.target.value})}
              required/>
          </div>
          <div className="space-y-2">
            <Label htmlFor="value">Giá trị</Label>
            <Input id="value" type="number" placeholder="e.g. 20" value={newCoupon.value}
                   onChange={(e) => setNewCoupon({...newCoupon, value: Number(e.target.value)})}
                   required/>
          </div>
          <div className="space-y-2">
            <Label htmlFor="quantity">Số lượng</Label>
            <Input id="value" type="number" placeholder="e.g. 20" value={newCoupon.quantity}
                   onChange={(e) => setNewCoupon({...newCoupon, quantity: Number(e.target.value)})}
                   required/>
          </div>
          <div className="space-y-2">
            <Label htmlFor="category">Loại</Label>
            <Select
              value={newCoupon.category}
              onValueChange={(value: string) => setNewCoupon({...newCoupon, category: value})}
            >
              <SelectTrigger>
                <SelectValue placeholder="Chọn loại hình"/>
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="FIXED">Cố định</SelectItem>
                <SelectItem value="RATE">Tỉ lệ</SelectItem>
              </SelectContent>
            </Select>
          </div>
          <Button type="submit" className="w-full">
            {coupon ? "Sửa " : "Thêm"}
          </Button>
        </form>
      </DialogContent>
    </Dialog>

  )
}