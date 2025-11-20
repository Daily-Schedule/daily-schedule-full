"use client"

import { Progress } from "@/components/ui/progress"
import { CheckCircle2, Circle, Clock } from "lucide-react"

export function YesterdayAnalysis() {
  const completedTasks = 7
  const totalTasks = 10
  const completionRate = (completedTasks / totalTasks) * 100
  const totalTime = 8.5

  const taskList = [
    { name: "팀 스탠드업 미팅", completed: true, duration: "30분" },
    { name: "프로젝트 기획서 작성", completed: true, duration: "2시간" },
    { name: "디자인 리뷰", completed: true, duration: "1시간" },
    { name: "코드 구현", completed: true, duration: "3시간" },
    { name: "클라이언트 피드백 정리", completed: false, duration: "-" },
    { name: "테스트 작성", completed: true, duration: "1.5시간" },
    { name: "문서 업데이트", completed: false, duration: "-" },
    { name: "주간 보고서 작성", completed: true, duration: "30분" },
    { name: "이메일 답장", completed: true, duration: "30분" },
    { name: "내일 일정 계획", completed: false, duration: "-" },
  ]

  return (
    <div className="space-y-6">
      <div className="grid gap-4 md:grid-cols-3">
        <div className="space-y-2">
          <div className="text-sm text-muted-foreground">완료율</div>
          <div className="text-3xl font-bold">{completionRate.toFixed(0)}%</div>
          <Progress value={completionRate} className="h-2" />
        </div>
        <div className="space-y-2">
          <div className="text-sm text-muted-foreground">완료한 일정</div>
          <div className="text-3xl font-bold">
            {completedTasks}/{totalTasks}
          </div>
        </div>
        <div className="space-y-2">
          <div className="text-sm text-muted-foreground">총 작업 시간</div>
          <div className="text-3xl font-bold">{totalTime}시간</div>
        </div>
      </div>

      <div className="pt-4">
        <h3 className="text-lg font-semibold mb-3">상세 내역</h3>
        <div className="space-y-2">
          {taskList.map((task, index) => (
            <div key={index} className="flex items-center gap-3 p-3 rounded-lg border bg-card">
              {task.completed ? (
                <CheckCircle2 className="h-5 w-5 text-primary flex-shrink-0" />
              ) : (
                <Circle className="h-5 w-5 text-muted-foreground flex-shrink-0" />
              )}
              <span className={`flex-1 ${!task.completed ? "text-muted-foreground" : ""}`}>{task.name}</span>
              <div className="flex items-center gap-1 text-sm text-muted-foreground">
                <Clock className="h-4 w-4" />
                <span className="font-mono">{task.duration}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
