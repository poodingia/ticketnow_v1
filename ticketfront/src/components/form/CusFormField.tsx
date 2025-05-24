"use client"

import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import type { Control, FieldValues, Path } from "react-hook-form"
import { ImageDropzone } from "@/components/dropzone/ImageDropzone"
import TiptapEditor from "@/components/editor/TipTapEditor"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

interface CustomFormFieldProps<T extends FieldValues> {
  name: Path<T>
  control: Control<T>
  label: string
  placeholder: string
  type?: string
  selects?: { value: string; label: string }[]
  disabled?: boolean
}

export default function CustomFormField<T extends FieldValues>({name, control, label, placeholder, type = "text", selects, disabled = false}: CustomFormFieldProps<T>) {
  return (
    <FormField
      control={control} name={name}
      render={({ field }) => (
        <FormItem>
          <FormLabel>{label}</FormLabel>
          {(type === "text" || type === "email" || type == "number") && (
            <FormControl><Input placeholder={placeholder} type={type} {...field} /></FormControl>
          )}
          {type === "datetime-local" && (
            <FormControl>
              <Input type="datetime-local" value={field.value || ""} onChange={(e) => field.onChange(e.target.value)} disabled={disabled}/>
            </FormControl>
          )}
          {type === "image" && <ImageDropzone onUpload={(url) => field.onChange(url)} value={field.value} />}
          {type === "editor" && <TiptapEditor value={field.value} onChange={field.onChange} />}
          {type === "select" && (
            <Select onValueChange={field.onChange} value={field.value || ""}>
              <SelectTrigger>
                <SelectValue placeholder={placeholder} />
              </SelectTrigger>
              <SelectContent>
                {selects?.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          )}
          <FormMessage />
        </FormItem>
      )}
    />
  )
}

