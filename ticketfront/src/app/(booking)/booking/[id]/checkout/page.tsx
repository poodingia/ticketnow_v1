"use client"

import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import PriceSection from "@/components/order/CheckoutPriceSection"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ArrowLeft, CreditCard, Loader2, ShoppingCart, User } from "lucide-react"
import { useEffect, useState } from "react"
import { useParams, useRouter } from "next/navigation"
import { createOrder } from "@/api/order"
import { useMutation } from "@tanstack/react-query"
import type { OrderItem } from "@/model/order.model"

export default function CheckoutPage() {
  const params = useParams<{ id: string }>()
  const router = useRouter()
  const [formData, setFormData] = useState({
    name: "", email: "", method: "", couponCode: "", price: 0,
    totalQuantity: 0, orderItems: [] as OrderItem[], eventId: params.id
  })
  const [isLoading, setIsLoading] = useState(true)

  const updateForm =
    (key: string, val: string | number) => setFormData(prev => ({ ...prev, [key]: val }))

  const mutation = useMutation({
    mutationFn: createOrder,
    onSuccess: (response) => {
      sessionStorage.clear()
      window.location.href = (response.data.vnpUrl)
    }
  })

  useEffect(() => {
    if (typeof window === "undefined") return
    try {
      setFormData(prev => ({
        ...prev,
        name: sessionStorage.getItem("checkout_name") || "",
        email: sessionStorage.getItem("checkout_email") || "",
        couponCode: sessionStorage.getItem("checkout_couponCode") || "",
        price: Number(sessionStorage.getItem("checkout_price") || 0),
        totalQuantity: Number(sessionStorage.getItem("checkout_quantity") || 0),
        orderItems: JSON.parse(sessionStorage.getItem("checkout_orderItems") || "[]")
      }))
    } catch (error) { console.error(error) }
    setIsLoading(false)
  }, [])

  useEffect(() => {
    if (typeof window === "undefined" || isLoading) return
    const { name, email, couponCode } = formData
    sessionStorage.setItem("checkout_name", name)
    sessionStorage.setItem("checkout_email", email)
    sessionStorage.setItem("checkout_couponCode", couponCode)
  }, [formData, isLoading])

  const handleOrder = () => {
    const { name, email, method, couponCode, price, totalQuantity, orderItems, eventId } = formData
    mutation.mutate({
      id: 0, price, quantity: totalQuantity, userId: 1, orderItems,
      email, name, couponCode: couponCode || undefined,
      eventId: Number(eventId), paymentMethod: method
    })
  }

  if (isLoading) return (
    <div className="flex items-center justify-center min-h-screen">
      <Loader2 className="h-8 w-8 animate-spin text-primary" />
    </div>
  )

  if (mutation.isPending || mutation.isSuccess) return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-background to-muted">
      <div className="w-full max-w-md bg-card rounded-xl shadow-lg p-8 text-center">
        <div className="flex justify-center mb-6">
          <Loader2 className="h-12 w-12 text-primary animate-spin" />
        </div>
        <h1 className="text-2xl font-bold">
          {mutation.isSuccess ? "Redirecting to Payment" : "Processing Your Order"}
        </h1>
      </div>
    </div>
  )

  const { name, email, method, couponCode, price, eventId } = formData
  const isFormValid = name.trim() && email.trim() && email.includes("@") && method

  return (
    <div className="min-h-screen bg-muted/30 py-10">
      <div className="container max-w-2xl mx-auto px-4">
        <div className="flex items-center justify-between mb-8">
          <Button variant="ghost" onClick={() => router.push(`/booking/${eventId}`)}>
            <ArrowLeft className="h-4 w-4 mr-2" />Quay lại chọn vé
          </Button>
          <h1 className="text-2xl font-bold">Checkout</h1>
        </div>

        <div className="flex flex-col gap-6">
          <Card className="shadow-md">
            <CardHeader className="border-b bg-muted/50">
              <CardTitle className="flex items-center gap-2">
                <ShoppingCart className="h-5 w-5 text-primary" />Đơn hàng
              </CardTitle>
            </CardHeader>
            <CardContent className="pt-6">
              <PriceSection price={price} eventId={eventId}
                            setPrice={val => updateForm("price", val)}
                            couponCode={couponCode}
                            setCouponCode={val => updateForm("couponCode", val)} />
            </CardContent>
          </Card>

          <Card className="shadow-md">
            <CardHeader className="border-b bg-muted/50">
              <CardTitle className="flex items-center gap-2">
                <User className="h-5 w-5 text-primary" />Thông tin khách hàng
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6 pt-6">
              <div className="space-y-2">
                <Label htmlFor="name">Họ tên</Label>
                <Input id="name" value={name}
                       onChange={e => updateForm("name", e.target.value)}
                       placeholder="Nhập họ tên" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input id="email" type="email" value={email}
                       onChange={e => updateForm("email", e.target.value)}
                       placeholder="Nhập email" />
                <p className="text-sm text-muted-foreground">Chúng tôi sẽ gửi vé của bạn đến đây.</p>
              </div>
            </CardContent>
          </Card>

          <Card className="shadow-md">
            <CardHeader className="border-b bg-muted/50">
              <CardTitle className="flex items-center gap-2">
                <CreditCard className="h-5 w-5 text-primary" />Phương thức
              </CardTitle>
            </CardHeader>
            <CardContent className="pt-6">
              <Label htmlFor="payment-method">Lựa chọn phương thức</Label>
              <Select onValueChange={val => updateForm("method", val)}>
                <SelectTrigger id="payment-method">
                  <SelectValue placeholder="Chọn một phương thức" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="vnpay">Thẻ</SelectItem>
                  <SelectItem value="payos">Quét QR</SelectItem>
                </SelectContent>
              </Select>
            </CardContent>
          </Card>

          <Button onClick={handleOrder} className="w-full h-12 mt-2" disabled={!isFormValid}>
            <ShoppingCart className="mr-2 h-5 w-5" />Hoàn tất
          </Button>
        </div>
      </div>
    </div>
  )
}