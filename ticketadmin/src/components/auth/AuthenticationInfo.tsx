'use client'

import Link from 'next/link';
import { useEffect, useState } from 'react';
import { DropdownMenu, DropdownMenuTrigger, DropdownMenuContent, DropdownMenuItem } from '@/components/ui/dropdown-menu'
import {LogOut} from "lucide-react";

export default function AuthenticationInfo() {
  type AuthenticatedUser = {
    username: string;
  };

  type AuthenticationInfoVm = {
    isAuthenticated: boolean;
    authenticatedUser: AuthenticatedUser;
  };

  const [authenticatedInfoVm, setAuthenticatedInfoVm] = useState<AuthenticationInfoVm>({
    isAuthenticated: false,
    authenticatedUser: { username: '' },
  });

  async function getAuthenticationInfo(): Promise<AuthenticationInfoVm> {
    const res = await fetch(`/authentication`);
    return await res.json();
  }

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

  useEffect(() => {
    getAuthenticationInfo().then((data) => {
      setAuthenticatedInfoVm(data);
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <>
      {authenticatedInfoVm.isAuthenticated ? (
        <DropdownMenu>
          <DropdownMenuTrigger className="bg-transparent border-none text-gray-400 hover:text-white focus:outline-none">
            <span>{authenticatedInfoVm.authenticatedUser.username}</span>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="bg-gray-800 border-none text-white mt-2 rounded-md shadow-lg">
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
        <div>
          <Link href={"/oauth2/authorization/adminbff"} className="text-gray-400 hover:text-white">Đăng nhập</Link>
        </div>
      )}
    </>
  );
}