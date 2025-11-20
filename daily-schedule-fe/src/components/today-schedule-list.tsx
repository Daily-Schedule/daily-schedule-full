"use client"

import { useState } from "react"
import { Checkbox } from "@/components/ui/checkbox"

type Schedule = {
  id: string
  time: string
  content: string
  completed: boolean
}

export function TodayScheduleList() {
  const [schedules, setSchedules] = useState<Schedule[]>([
    { id: "1", time: "09:00", content: "팀 회의 참석", completed: true },
    { id: "2", time: "10:30", content: "프로젝트 기획서 작성", completed: false },
    { id: "3", time: "14:00", content: "클라이언트 미팅", completed: false },
    { id: "4", time: "16:00", content: "코드 리뷰 및 피드백", completed: false },
  ])

  const toggleSchedule = (id: string) => {
    setSchedules(
      schedules.map((schedule) => (schedule.id === id ? { ...schedule, completed: !schedule.completed } : schedule)),
    )
  }

  return (
    <div className="space-y-3">
      {schedules.length === 0 ? (
        <p className="text-center text-muted-foreground py-8">등록된 일정이 없습니다</p>
      ) : (
        schedules.map((schedule) => (
          <div
            key={schedule.id}
            className="flex items-center gap-3 p-3 rounded-lg border bg-card hover:bg-accent/50 transition-colors"
          >
            <Checkbox
              checked={schedule.completed}
              onCheckedChange={() => toggleSchedule(schedule.id)}
              id={schedule.id}
            />
            <label htmlFor={schedule.id} className="flex-1 flex items-center gap-3 cursor-pointer">
              <span className="text-sm font-mono text-muted-foreground min-w-[50px]">{schedule.time}</span>
              <span className={schedule.completed ? "line-through text-muted-foreground" : ""}>{schedule.content}</span>
            </label>
          </div>
        ))
      )}
    </div>
  )
}
