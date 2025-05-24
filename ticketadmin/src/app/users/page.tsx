'use client'

import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Check, Search, Trash2, UserCog, X} from "lucide-react";
import {useState} from "react";
import {useAllUserProfiles} from "@/hooks/useFetch";
import {deleteUserProfile, updateUserRoles} from "@/api/feedback";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Badge} from "@/components/ui/badge";

export default function Home() {
  const [searchTerm, setSearchTerm] = useState("")
  const [email, setEmail] = useState("")
  const usersData = useAllUserProfiles();
  const handleDelete = (id: string) => {
    deleteUserProfile(id).then(() => usersData.refetch())
  }
  const handleUpdate = (email: string) => {
    updateUserRoles(email).then(() => usersData.refetch());
  }

  return (
    <div className="w-2/3 mx-auto">
      <CardHeader>
        <CardTitle>Quản lý người dùng</CardTitle>
        <CardDescription>Tìm kiếm và quản lý tài khoản người dùng trong hệ thống</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col space-y-4">
          <div className="flex justify-center items-center gap-2">
            <div className="w-1/2">
              <Input
                placeholder="Nhập email người dùng"
                type="email"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full"
              />
            </div>
            <Button onClick={() => setEmail(searchTerm)} className="whitespace-nowrap">
              <Search className="h-4 w-4 mr-2" /> Tìm kiếm
            </Button>
          </div>

          {usersData.isSuccess &&
            usersData.data?.filter((user) => user.email.toLowerCase().includes(email.toLowerCase())).length > 0 && (
              <div className="rounded-md border overflow-hidden">
                <Table className="text-sm">
                  <TableHeader>
                    <TableRow>
                      <TableHead>Email</TableHead>
                      <TableHead>Họ</TableHead>
                      <TableHead>Tên</TableHead>
                      <TableHead>Quản trị viên</TableHead>
                      <TableHead className="text-center">Thao tác</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {usersData.data
                      ?.filter((user) => user.email.toLowerCase().includes(email.toLowerCase())).map((user) => (
                        <TableRow key={user.id} className="hover:bg-muted/50 cursor-pointer">
                          <TableCell className="font-medium">{user.email}</TableCell>
                          <TableCell>{user.lastName}</TableCell>
                          <TableCell>{user.firstName}</TableCell>
                          <TableCell>
                            {user.isAdmin ? (
                              <Badge variant="default" className="bg-green-500 hover:bg-green-600">
                                <Check className="h-3 w-3 mr-1" /> Admin
                              </Badge>
                            ) : (
                              <Badge variant="outline" className="text-muted-foreground">
                                <X className="h-3 w-3 mr-1" /> User
                              </Badge>
                            )}
                          </TableCell>
                          <TableCell>
                            <div className="text-center">
                              <Button variant="secondary" size="sm" onClick={() => handleUpdate?.(user.email)} className="mr-5">
                                <UserCog className="h-3.5 w-3.5 mr-1" />
                                {user.isAdmin ? "Hủy Admin" : "Đặt Admin"}
                              </Button>
                              <Button variant="destructive" size="sm" onClick={() => handleDelete?.(user.id)}>
                                <Trash2 className="h-3.5 w-3.5 mr-1" />
                                Xóa
                              </Button>
                            </div>
                          </TableCell>
                        </TableRow>
                      ))}
                  </TableBody>
                </Table>
              </div>
            )}
        </div>
      </CardContent>
    </div>
  )
}
