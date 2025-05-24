'use client'

import {useState} from "react"
import {Button} from "@/components/ui/button"
import {CheckCircle, CircleX, Home} from 'lucide-react';
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle,} from "@/components/ui/card"
import {Badge} from "@/components/ui/badge"
import Link from "next/link";
import {useQuery} from "@tanstack/react-query";
import { updatePaymentStatus} from "@/api/order";
import {motion} from "framer-motion";
import {Separator} from "@/components/ui/separator";
import {useSearchParams} from "next/navigation";

export default function PaymentPage() {
  const [showConfetti, setShowConfetti] = useState(false)

  const searchParams = useSearchParams();

  const params = {
    responseCode: searchParams.get('vnp_ResponseCode') || searchParams.get('code') || '',
    transactionId: searchParams.get('vnp_TransactionNo') || searchParams.get('id') || '',
    ref: searchParams.get('vnp_TxnRef') || searchParams.get('orderCode') || '',
    status: searchParams.get('status')  || '',
  };

  const {} = useQuery({
    queryKey: ["updatePaymentStatus", params.ref, params.responseCode],
    queryFn: () => {
      updatePaymentStatus({ref: params.ref, code: params.responseCode, status: params.status});
      setShowConfetti(true)
    },
    enabled: !!params.ref && !!params.responseCode
  });

  if (params.status === 'CANCELLED' || params.responseCode === '24' || params.responseCode === '01') {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <Card className="w-full max-w-[800px] h-[300px] text-center flex flex-col items-center mb-60">
          <CardHeader className="flex flex-col items-center">
            <CircleX size={64}/>
            <CardTitle>Thanh toán bị hủy</CardTitle>
            <CardDescription>Thanh toán của bạn đã bị hủy. Vui lòng thử lại.</CardDescription>
          </CardHeader>
          <CardFooter className="flex justify-center">
            <Link href={`/`}>
              <Button>Quay về trang chủ</Button>
            </Link>
          </CardFooter>
        </Card>
      </div>
    );
  } else {
    return (
      <div
        className="flex justify-center items-center min-h-screen bg-gradient-to-b from-background to-muted/30 p-4">
        {showConfetti && <Confetti/>}

        <motion.div
          initial={{opacity: 0, y: 20}}
          animate={{opacity: 1, y: 0}}
          transition={{duration: 0.5}}
          className="w-full max-w-[500px]"
        >
        <Card className="border-2 shadow-lg">
          <CardHeader className="flex flex-col items-center gap-2 pb-2">
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{
                type: "spring",
                stiffness: 260,
                damping: 20,
                delay: 0.1,
              }}
              className="rounded-full bg-green-100 p-3 dark:bg-green-900/30"
            >
              <CheckCircle className="h-12 w-12 text-green-600 dark:text-green-500" />
            </motion.div>

            <div className="space-y-1 text-center mt-4">
              <CardTitle className="text-2xl font-bold">Thanh toán thành công!</CardTitle>
              <CardDescription className="text-base">
                Giao dịch của bạn đã được hoàn tất
              </CardDescription>
            </div>
          </CardHeader>

          <CardContent className="pb-2">
            <div className="rounded-lg bg-muted p-4">
              <div className="flex justify-between items-center">
                <span className="text-muted-foreground">Mã giao dịch</span>
                <Badge variant="outline" className="font-mono">
                  {params.transactionId}
                </Badge>
              </div>
            </div>

            <div className="mt-4 text-center text-sm text-muted-foreground">
              Cảm ơn bạn đã mua hàng. Vé của bạn đã được xác nhận.
            </div>
          </CardContent>

          <Separator />

          <CardFooter className="flex justify-center pt-4">
            <Link href="/" className="w-full">
              <Button className="w-full">
                <Home className="mr-2 h-4 w-4" />
                Quay về trang chủ
              </Button>
            </Link>
          </CardFooter>
        </Card>

        </motion.div>
      </div>
    )
  }
}

function Confetti() {
  return (
    <div className="fixed inset-0 pointer-events-none z-50">
      {[...Array(50)].map((_, i) => (
        <motion.div
          key={i}
          className="absolute"
          initial={{
            top: "-10%",
            left: `${Math.random() * 100}%`,
            scale: Math.random() * 0.5 + 0.5,
          }}
          animate={{
            top: "100%",
            rotate: Math.random() * 360,
          }}
          transition={{
            duration: Math.random() * 2 + 2,
            ease: "linear",
            delay: Math.random() * 0.5,
          }}
          style={{
            width: "10px",
            height: "10px",
            borderRadius: Math.random() > 0.5 ? "50%" : "0%",
            backgroundColor: `hsl(${Math.random() * 360}, 80%, 60%)`,
          }}
        />
      ))}
    </div>
  )
}
