"use client"

import {EditorContent, useEditor} from "@tiptap/react"
import StarterKit from "@tiptap/starter-kit"
import Image from "@tiptap/extension-image";
import {Bold, Heading1, Heading2, ImageIcon, Italic, List, ListOrdered} from "lucide-react"
import {Button} from "@/components/ui/button"
import {uploadImage} from "@/api/event";
import {Input} from "@/components/ui/input";
import React from "react";

interface TiptapEditorProps {
  value: string
  onChange(value: string): void
}

export default function TiptapEditor({value, onChange}: TiptapEditorProps) {
  const editor = useEditor({
    extensions: [StarterKit, Image],
    content: value,
    immediatelyRender: false,
    onUpdate: ({editor}) => {
      onChange(editor.getHTML())
    },
    editorProps: {
      attributes: {
        class: 'focus:outline-none',
      },
    }
  })

  const handleImageUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;
    const data = await uploadImage(file);
    if (data) {editor?.commands.setImage({src: data});}
  };

  if (!editor) {
    return null
  }

  return (
    <div className="overflow-hidden bg-card rounded-md shadow-sm">
      <div className="bg-muted/50 flex gap-2 flex-wrap">
        <Button
          type="button" variant="ghost" size="icon"
          onClick={() => editor.chain().focus().toggleBold().run()}
          className={editor.isActive("bold") ? "bg-muted-foreground/20" : ""}
        >
          <Bold className="h-4 w-4"/>
        </Button>
        <Button
          type="button" variant="ghost" size="icon"
          onClick={() => editor.chain().focus().toggleItalic().run()}
          className={editor.isActive("italic") ? "bg-muted-foreground/20" : ""}
        >
          <Italic className="h-4 w-4"/>
        </Button>
        <Button
          type="button" variant="ghost" size="icon"
          onClick={() => editor.chain().focus().toggleHeading({level: 1}).run()}
          className={editor.isActive("heading", {level: 1}) ? "bg-muted-foreground/20" : ""}
        >
          <Heading1 className="h-4 w-4"/>
        </Button>
        <Button
          type="button" variant="ghost" size="icon"
          onClick={() => editor.chain().focus().toggleHeading({level: 2}).run()}
          className={editor.isActive("heading", {level: 2}) ? "bg-muted-foreground/20" : ""}
        >
          <Heading2 className="h-4 w-4"/>
        </Button>
        <Button
          type="button" variant="ghost" size="icon"
          onClick={() => editor.chain().focus().toggleBulletList().run()}
          className={editor.isActive("bulletList") ? "bg-muted-foreground/20" : ""}
        >
          <List className="h-4 w-4"/>
        </Button>
        <Button
          type="button" variant="ghost" size="icon"
          onClick={() => editor.chain().focus().toggleOrderedList().run()}
          className={editor.isActive("orderedList") ? "bg-muted-foreground/20" : ""}
        >
          <ListOrdered className="h-4 w-4"/>
        </Button>
        <Button type="button" variant="ghost" size="icon" className="relative">
          <ImageIcon className="h-4 w-4"/>
          <Input
            type="file" accept="image/*"
            onChange={handleImageUpload} className="absolute inset-0 opacity-0 cursor-pointer"
          />
        </Button>
      </div>
      <div className="p-2 bg-background border-2">
        <EditorContent editor={editor}
                       className="prose dark:prose-invert max-w-none min-h-[200px] m-0 leading-normal"/>
      </div>
    </div>
  )
}

