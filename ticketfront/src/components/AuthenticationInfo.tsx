"use client"

import Link from 'next/link';
import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from '@/components/ui/dropdown-menu'
import {Button} from '@/components/ui/button';
import {Avatar} from "@/components/ui/avatar";
import {AvatarFallback} from "@radix-ui/react-avatar";
import {DropdownMenuSeparator} from "@radix-ui/react-dropdown-menu";
import {LogOut, MessageCircle, Package, ShoppingCart, User} from "lucide-react";
import {useAuthenticate} from "@/hooks/useFetch";

export default function AuthenticationInfo() {
  const { data: authenticatedInfoVm, isPending } = useAuthenticate()

  async function logout(): Promise<void> {
    try {
      await fetch("/logout", {
        method: "POST",
        credentials: 'include'
      });
    } catch (e) {
      console.error(e);
      window.location.href = "/";
    }
  }

  if (isPending) {
    return <div className="h-10 w-10 rounded-full bg-zinc-800 animate-pulse"></div>
  }

  return (
    <>
      {authenticatedInfoVm?.isAuthenticated ? (
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="relative h-10 w-10 rounded-full p-0 hover:bg-zinc-800">
              <Avatar className="h-10 w-10 border-2 border-zinc-700">
                <AvatarFallback className="text-white m-auto">
                  {authenticatedInfoVm.authenticatedUser?.username.charAt(0).toUpperCase()}
                </AvatarFallback>
              </Avatar>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="w-56 bg-zinc-900 border-zinc-800 text-white" align="end" forceMount>
            <DropdownMenuItem className="focus:bg-zinc-800 focus:text-white">
              <div className="flex flex-col space-y-1 py-1">
                <p className="text-sm font-medium leading-none">{authenticatedInfoVm.authenticatedUser?.username}</p>
                <p className="text-xs text-gray-400">Người dùng đăng nhập</p>
              </div>
            </DropdownMenuItem>
            <DropdownMenuSeparator className="bg-zinc-800" />
            <DropdownMenuItem asChild>
              <Link
                href="/organizer"
                className="flex items-center py-2 cursor-pointer focus:bg-zinc-800 focus:text-white"
              >
                <Package className="mr-2 h-4 w-4 text-gray-400" />
                <span>Nhà tổ chức</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link href="/order" className="flex items-center py-2 cursor-pointer focus:bg-zinc-800 focus:text-white">
                <ShoppingCart className="mr-2 h-4 w-4 text-gray-400" />
                <span>Đơn hàng</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link
                href="/feedback"
                className="flex items-center py-2 cursor-pointer focus:bg-zinc-800 focus:text-white"
              >
                <MessageCircle className="mr-2 h-4 w-4 text-gray-400" />
                <span>Khiếu nại</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuItem asChild>
              <Link
                href="/profile"
                className="flex items-center py-2 cursor-pointer focus:bg-zinc-800 focus:text-white"
              >
                <User className="mr-2 h-4 w-4 text-gray-400" />
                <span>Hồ sơ</span>
              </Link>
            </DropdownMenuItem>
            <DropdownMenuSeparator className="bg-zinc-800" />
            <DropdownMenuItem
              className="text-red-400 focus:text-red-300 focus:bg-zinc-800 py-2 cursor-pointer"
              onSelect={() => logout()}
            >
              <LogOut className="mr-2 h-4 w-4" />
              <span>Đăng xuất</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      ) : (
        <Button variant="outline" className="bg-black hover:bg-zinc-800 text-white hover:text-white border-zinc-700" asChild>
          <Link href={"/oauth2/authorization/gateway"}>Đăng nhập</Link>
        </Button>
      )}
    </>
  )
}

