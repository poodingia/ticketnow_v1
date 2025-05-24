import  {Suspense} from "react";
import AuthenticationInfo from "@/components/auth/AuthenticationInfo";
import Link from "next/link";

export default function Header() {

  return (
    <header className=" bg-[#1C2449] border-b">
      <div className="container mx-auto px-4 py-4 flex items-center justify-between text-white">
        <Link href="/" className="text-2xl font-bold text-white">
          Hệ thống quản trị vé sự kiện
        </Link>
        <div className="flex items-center space-x-4 ml-auto">
          <Link href="/events" >
            Sự kiện
          </Link>
          <Link href="/users" >
            Người dùng
          </Link>
          <Link href="/feedbacks" >
            Khiếu nại
          </Link>
          <Suspense><AuthenticationInfo/></Suspense>
        </div>
      </div>
    </header>
  )
}