"use client"

import type React from "react"
import {useState} from "react"
import Link from "next/link"
import {Search} from "lucide-react"
import {Input} from "@/components/ui/input"
import AuthenticationInfo from "@/components/AuthenticationInfo";
import {useRouter} from "next/navigation";

const Header = () => {
  const router = useRouter()
  const [searchQuery, setSearchQuery] = useState("")

  const categories = new Map<string, string>([["Concert", "Hòa nhạc"], ["Sport", "Thể thao"], ["Theater", "Kịch"], ["Festival", "Lễ hội"]])

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault()
    router.push(`/search?q=${searchQuery}`)
  }

  return (<>
      <header className="bg-[#1C2449] border-b border-zinc-800 sticky top-0 z-50 backdrop-blur-sm bg-opacity-95">
        <div className="container mx-auto px-4 py-3 flex items-center justify-between">
          <Link href="/" className="text-2xl font-bold text-white hover:text-gray-300 transition-colors">
            Hệ thống bán vé
          </Link>

          <form onSubmit={handleSearch} className="flex-1 max-w-md mx-4">
            <div className="relative">
              <Input
                type="text"
                placeholder="Tìm kiếm sự kiện..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 bg-zinc-900 border-zinc-800 text-white placeholder:text-gray-400 focus-visible:ring-gray-700 transition-all"
              />
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400"/>
            </div>
          </form>

          <div className="flex items-center">
            <AuthenticationInfo/>
          </div>
        </div>
        <nav className="bg-[#141A36] border-b border-zinc-800 py-2 overflow-x-auto">
          <div className="container mx-auto px-4">
            <ul className="flex items-center space-x-1 md:space-x-2">
              {Array.from(categories).map(([key, value]) => (
                <li key={key}>
                  <Link href={`/search?category=${key}`} className="text-white text-sm hover:text-gray-300 transition-colors pr-4">
                    {value}
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        </nav>
      </header>
    </>)
}

export default Header

