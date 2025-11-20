"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Plus } from "lucide-react"

export function TomorrowScheduleForm() {
  const [startTime, setStartTime] = useState("")
  const [endTime, setEndTime] = useState("")
  const [content, setContent] = useState("")

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("[v0] Schedule added:", { startTime, endTime, content })
    // Reset form
    setStartTime("")
    setEndTime("")
    setContent("")
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="start-time">시작 시간</Label>
          <Input
            id="start-time"
            type="time"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            required
          />
        </div>
        <div className="space-y-2">
          <Label htmlFor="end-time">종료 시간</Label>
          <Input id="end-time" type="time" value={endTime} onChange={(e) => setEndTime(e.target.value)} required />
        </div>
      </div>
      <div className="space-y-2">
        <Label htmlFor="content">일정 내용</Label>
        <Input
          id="content"
          type="text"
          placeholder="일정을 입력하세요"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />
      </div>
      <Button type="submit" className="w-full">
        <Plus className="h-4 w-4 mr-2" />
        일정 추가
      </Button>
    </form>
  )
}
