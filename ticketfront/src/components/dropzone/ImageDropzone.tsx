"use client"

import { useCallback } from "react"
import { useDropzone } from "react-dropzone"
import Image from "next/image"
import { Upload, X } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"

interface ImageDropzoneProps {
  onUpload(url: string) : void
  value: string
}

export function ImageDropzone({ onUpload, value }: ImageDropzoneProps) {
  const onDrop = useCallback(
    async (acceptedFiles: File[]) => {
      const file = acceptedFiles[0]
      if (file) {
        const reader = new FileReader()
        reader.onloadend = () => {
          onUpload(reader.result as string)
        }
        reader.readAsDataURL(file)
      }
    },
    [onUpload],
  )

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {"image/*": [".jpeg", ".jpg", ".png", ".gif"],},
    multiple: false,
  })

  const removeImage = () => {
    onUpload("")
  }

  return (
    <Card>
      <CardContent className="p-6">
        {value ? (
          <div className="relative">
            <Image
              src={value || "/placeholder.svg"} alt="Uploaded image" width={600} height={400}
              className="w-full h-[300px] object-cover rounded-md"
            />
            <Button variant="destructive" size="icon" className="absolute top-2 right-2" onClick={removeImage}>
              <X className="h-4 w-4" />
            </Button>
          </div>
        ) : (
          <div
            {...getRootProps()}
            className={`border-2 border-dashed rounded-md p-8 text-center cursor-pointer transition-colors ${
              isDragActive ? "border-primary bg-primary/10" : "border-muted hover:border-primary"
            }`}
          >
            <input {...getInputProps()} />
            <div className="flex flex-col items-center justify-center">
              <Upload className="w-12 h-12 text-muted-foreground mb-4" />
              <p className="text-lg font-semibold mb-2">
                {isDragActive ? "Kéo ảnh vào đây" : "Kéo và thả ảnh vào đây"}
              </p>
              <p className="text-sm text-muted-foreground">hoặc nhấn để chọn file</p>
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  )
}

