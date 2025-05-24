import "./globals.css";
import React from "react";
import Header from "@/components/layout/Header";
import { Be_Vietnam_Pro } from "next/font/google";
import {ReactQueryProvider} from "@/app/provider";
import {Toaster} from "@/components/ui/toaster";

const beVietnamPro = Be_Vietnam_Pro({
  weight: '400',
  subsets: ['latin'],
})

export const metadata = {
  title: "TicketAdmin", description: "Your one-stop shop for event tickets",
}

export default function RootLayout({children,}: Readonly<{ children: React.ReactNode; }>) {
  return (
    <html lang="en">
      <body className={`${beVietnamPro.className}`}>
        <ReactQueryProvider>
          <Header/>
          {children}
        </ReactQueryProvider>
        <Toaster/>
      </body>
    </html>
  );
}
