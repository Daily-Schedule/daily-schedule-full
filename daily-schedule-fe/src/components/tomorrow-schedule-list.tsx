"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { X } from "lucide-react"

type Schedule = {
  id: string
  startTime: string
  endTime: string
  content: string
}

export function TomorrowScheduleList() {
  const [schedules, setSchedules] = useState<Schedule[]>([
    { id: "1", startTime: "09:00", endTime: "10:00", content: "아침 운동" },
    { id: "2", startTime: "10:30", endTime: "12:00", content: "업무 미팅" },
    { id: "3", startTime: "13:00", endTime: "14:30", content: "프로젝트 개발" },
  ])

  const deleteSchedule = (id: string) => {
    setSchedules(schedules.filter((schedule) => schedule.id !== id))
  }

  return (
    <div className="space-y-3">
      {schedules.length === 0 ? (
        <p className="text-center text-muted-foreground py-8">등록된 일정이 없습니다</p>
      ) : (
        schedules.map((schedule) => (
          <div
            key={schedule.id}
            className="flex items-center gap-3 p-3 rounded-lg border bg-card hover:bg-accent/50 transition-colors group"
          >
            <div className="flex-1 flex items-center gap-3">
              <span className="text-sm font-mono text-muted-foreground whitespace-nowrap">
                {schedule.startTime} - {schedule.endTime}
              </span>
              <span>{schedule.content}</span>
            </div>
            <Button
              variant="ghost"
              size="icon"
              onClick={() => deleteSchedule(schedule.id)}
              className="opacity-0 group-hover:opacity-100 transition-opacity"
            >
              <X className="h-4 w-4" />
            </Button>
          </div>
        ))
      )}
    </div>
  )
}
