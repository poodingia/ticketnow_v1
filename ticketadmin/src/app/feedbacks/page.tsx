"use client"

import { useState } from "react"
import { useFeedbacks } from "@/hooks/useFetch"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { CheckCircle, XCircle, AlertCircle, Search, Filter } from "lucide-react"
import Link from "next/link";

export default function Home() {
  const feedbackData = useFeedbacks()
  const [searchTerm, setSearchTerm] = useState("")
  const [statusFilter, setStatusFilter] = useState("all")

  const getStatusIcon = (status: string) => {
    switch (status) {
      case "resolved":
        return <CheckCircle className="w-4 h-4 text-green-500" />
      case "pending":
        return <AlertCircle className="w-4 h-4 text-yellow-500" />
      case "closed":
        return <XCircle className="w-4 h-4 text-red-500" />
      default:
        return null
    }
  }

  const filteredFeedback =
    feedbackData.data?.filter(
      (feedback) =>
        (statusFilter === "all" || feedback.status === statusFilter) &&
        (feedback.topic.toLowerCase().includes(searchTerm.toLowerCase()) ||
          feedback.message.toLowerCase().includes(searchTerm.toLowerCase())),
    ) || []

  return (
    <div className="container mx-auto px-4 py-8">
      <Card className="mb-8">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="text-3xl font-bold">Quản lý khiếu nại</CardTitle>
          </div>
          <CardDescription>Quản lý và phân tích một cách hiệu quả</CardDescription>
        </CardHeader>
        <CardContent>
              <div className="flex justify-between items-center mb-4">
                <div className="flex items-center space-x-2">
                  <Input
                    placeholder="Tìm kiếm..." className="w-64" value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                  />
                  <Button size="icon" variant="outline">
                    <Search className="h-4 w-4" />
                  </Button>
                </div>
                <div className="flex items-center space-x-2">
                  <Select value={statusFilter} onValueChange={setStatusFilter}>
                    <SelectTrigger className="w-[180px]">
                      <SelectValue placeholder="Filter by status" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">Tất cả</SelectItem>
                      <SelectItem value="pending">Đang chờ</SelectItem>
                      <SelectItem value="resolved">Đã xong</SelectItem>
                      <SelectItem value="rejected">Từ chỗi</SelectItem>
                    </SelectContent>
                  </Select>
                  <Button size="icon" variant="outline">
                    <Filter className="h-4 w-4" />
                  </Button>
                </div>
              </div>
              {feedbackData.isLoading && <p className="text-center py-4">Loading feedback data...</p>}
              {feedbackData.isError && (<p className="text-center py-4 text-red-500">Error: {feedbackData.error.message}</p>)}
              {feedbackData.isSuccess && (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>ID</TableHead>
                      <TableHead>Chủ đề</TableHead>
                      <TableHead>Ngày</TableHead>
                      <TableHead>Trạng thái</TableHead>
                      <TableHead></TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredFeedback.map((feedback) => (
                      <TableRow key={feedback.id}>
                        <TableCell className="font-medium">{feedback.id}</TableCell>
                        <TableCell>{feedback.topic}</TableCell>
                        <TableCell>{new Date("2025-01-01T12:12:12").toLocaleDateString()}</TableCell>
                        <TableCell>
                          <Badge
                            variant={feedback.status === "resolved" ? "default" : "outline"}
                            className="flex items-center gap-1 w-fit"
                          >
                            {getStatusIcon(feedback.status.toLowerCase())}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <Button variant="outline" className="bg-[#1C2449]  text-white border-zinc-700" asChild>
                            <Link href={`/feedbacks/${feedback.id}`}  >
                              Xem
                            </Link>
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              )}
        </CardContent>
      </Card>
    </div>
  )
}

