import React from "react";
import Header from "@/components/layout/Header";
import Footer from "@/components/layout/Footer";

export default function StoreLayout({children,}: { children: React.ReactNode }) {
  return(
    <>
      <Header/>
      {children}
      <Footer/>
    </>
  )
}
