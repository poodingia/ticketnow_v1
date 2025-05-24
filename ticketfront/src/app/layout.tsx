import {Be_Vietnam_Pro} from "next/font/google"
import "./globals.css"
import {ReactQueryProvider} from "@/app/providers";
import {Toaster} from "@/components/ui/toaster";
import React from "react";

const beVietnamPro = Be_Vietnam_Pro({
  weight: '400',
  subsets: ['latin'],
})

export const metadata = {
  title: "TicketNow", description: "Your one-stop shop for event tickets",
}

export default function RootLayout({children,}: { children: React.ReactNode }) {
  return (
    <html lang="en">
    <body className={`${beVietnamPro.className} flex flex-col min-h-screen`}>
      <ReactQueryProvider>
        {children}
      </ReactQueryProvider>
        <Toaster/>
      </body>
    </html>
  )
}

