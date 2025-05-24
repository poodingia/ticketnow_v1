'use client'

import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { TicketType } from "@/model/ticketType.model";
import React from "react";
import { CardContent } from "@/components/ui/card";

interface TicketTypeTableProps {
  ticketTypes: TicketType[] | undefined;
}

export default function TicketTypeTable({ ticketTypes }: TicketTypeTableProps) {
  return (
    <CardContent className="p-0 sm:p-2">
      <div className="text-sm font-medium text-muted-foreground mb-2 px-3 pt-3">Hạng vé</div>
      <div className="rounded-md border">
        <Table className="min-w-full divide-y divide-gray-200">
          <TableHeader className="bg-gray-50">
            <TableRow>
              <TableHead className="w-[60px] font-medium">ID</TableHead>
              <TableHead className="font-medium">Giá</TableHead>
              <TableHead className="font-medium">Số lượng</TableHead>
              <TableHead className="font-medium text-right">Mô tả</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody className="bg-white divide-y divide-gray-200">
            {ticketTypes?.map((ticketType) => (
              <TableRow key={ticketType.id} className="hover:bg-muted/50">
                <TableCell className="font-medium">{ticketType.id}</TableCell>
                <TableCell className="font-medium">{ticketType.price}</TableCell>
                <TableCell className="font-medium">{ticketType.quantity}</TableCell>
                <TableCell className="text-right text-muted-foreground">{ticketType.description}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </CardContent>
);
}