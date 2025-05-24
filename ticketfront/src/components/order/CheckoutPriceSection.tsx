import { useState } from "react"
import { Check, X } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Separator } from "@/components/ui/separator"
import {fetchCouponDetail} from "@/api/event";

interface PriceSectionProps {
  price: number,
  eventId: string,
  setPrice: (price: number) => void,
  couponCode?: string,
  setCouponCode: (couponCode: string) => void
}

export default function PriceSection({price, eventId, setPrice, setCouponCode}: PriceSectionProps) {
  const [isValidCoupon, setIsValidCoupon] = useState<boolean | null>(null)
  const [couponInput, setCouponInput] = useState("")
  const [oldPrice] = useState(price)

  const validateCoupon = async () => {
    try {
      const coupon = await fetchCouponDetail(couponInput, eventId);
      setIsValidCoupon(true)
      setCouponCode(coupon.id.toString())
      switch (coupon.category) {
        case "RATE":
          setPrice(Math.max(oldPrice - (oldPrice * coupon.value) / 100, 2000))
          break
        case "FIXED":
          setPrice(Math.max(oldPrice - coupon.value, 2000))
          break
        default:
          setPrice(oldPrice)
      }
    } catch {
      setIsValidCoupon(false)
      setPrice(oldPrice)
    }
  }

  return (
        <div className="space-y-6">
          <div className="flex justify-between items-center">
            <span className="text-muted-foreground">Giá gốc</span>
            <span className="font-medium">{oldPrice} VND</span>
          </div>

          <div className="space-y-2">
            <Label htmlFor="coupon">Mã giảm giá</Label>
            <div className="flex gap-2">
              <Input
                id="coupon"
                placeholder="Nhập mã"
                value={couponInput}
                onChange={(e) => {
                  setCouponInput(e.target.value)
                  setIsValidCoupon(null)
                }}
              />
              <Button onClick={validateCoupon} variant="secondary">
                Áp dụng
              </Button>
            </div>

            {isValidCoupon !== null && (
              <div className={`flex items-center gap-2 text-sm ${isValidCoupon ? "text-green-600" : "text-red-600"}`}>
                {isValidCoupon ? (
                  <>
                    <Check className="h-4 w-4" />
                    <span>Áp dụng thành công</span>
                  </>
                ) : (
                  <>
                    <X className="h-4 w-4" />
                    <span>Mã không hợp lệ</span>
                  </>
                )}
              </div>
            )}
          </div>

          <Separator />

          <div className="space-y-2">
            <div>
              {oldPrice > price && <p className="text-sm text-right text-green-600">- {oldPrice - price} VND</p>}
            </div>
            <div className="flex justify-between items-center">
              <span className="font-semibold">Giá sau ưu đãi</span>
              <span className="font-bold text-lg">{price} VND</span>
            </div>
          </div>
        </div>
  )
}