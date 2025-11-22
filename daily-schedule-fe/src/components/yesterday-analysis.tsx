"use client"

import { CheckCircle2, XCircle, AlertCircle, ArrowRight } from "lucide-react"
import { useMemo } from "react"




const DUMMY_DATA: YesterdayResponseDto = {
  startDelayMinutes: 15, 
  totalDurationMinutes: 380, 
  taskDurations: [
    { title: "팀 스탠드업 미팅", plannedDurationMinutes: 30, actualDurationMinutes: 35 },
    { title: "API 설계", plannedDurationMinutes: 60, actualDurationMinutes: 50 },
    { title: "핵심 로직 구현", plannedDurationMinutes: 120, actualDurationMinutes: 180 },
    { title: "코드 리뷰", plannedDurationMinutes: 40, actualDurationMinutes: 40 },
  ],
  unfinishedTodoTitles: ["테스트 코드 작성", "문서 정리"],
}

// 헬퍼 함수
const formatDuration = (minutes: number) => {
  const h = Math.floor(minutes / 60)
  const m = minutes % 60
  if (h === 0) return `${m}분`
  return `${h}시간 ${m}분`
}

interface Props {
  data?: YesterdayResponseDto
}

export function YesterdayAnalysis({ data = DUMMY_DATA }: Props) {
  const totalTasks = data.taskDurations.length + data.unfinishedTodoTitles.length
  const completedTasks = data.taskDurations.length
  const completionRate = totalTasks > 0 ? (completedTasks / totalTasks) * 100 : 0

  const startStatus = useMemo(() => {
    if (data.startDelayMinutes > 0) return { label: "지각", color: "text-red-500", bg: "bg-red-100" }
    if (data.startDelayMinutes < 0) return { label: "일찍 시작", color: "text-green-600", bg: "bg-green-100" }
    return { label: "정시 시작", color: "text-blue-600", bg: "bg-blue-100" }
  }, [data.startDelayMinutes])

  return (
    <div className="space-y-8">
      {/* 상단 요약 카드 그리드 */}
      <div className="grid gap-4 md:grid-cols-3">
        {/* 시작 시간 분석 */}
        <div className="rounded-xl border bg-card p-6 shadow-sm flex flex-col justify-between">
          <div className="text-sm font-medium text-muted-foreground">첫 일정 시작</div>
          <div className="mt-2 flex items-end gap-2">
            <div className={`text-2xl font-bold ${startStatus.color}`}>
              {Math.abs(data.startDelayMinutes)}분
            </div>
            <span className={`mb-1 text-xs font-medium px-2 py-0.5 rounded-full ${startStatus.bg} ${startStatus.color}`}>
              {startStatus.label}
            </span>
          </div>
          <p className="text-xs text-muted-foreground mt-2">
            {data.startDelayMinutes > 0 
              ? "계획보다 늦게 시작했어요 😅" 
              : "상쾌한 시작이었네요! ☀️"}
          </p>
        </div>

        {/* 완료율 */}
        <div className="rounded-xl border bg-card p-6 shadow-sm flex flex-col">
          <div className="text-sm font-medium text-muted-foreground">일정 달성률</div>
          <div className="mt-2">
            <span className="text-2xl font-bold">{completionRate.toFixed(0)}%</span>
            <span className="text-sm text-muted-foreground ml-2">
              ({completedTasks}/{totalTasks})
            </span>
          </div>
          <div className="w-full bg-secondary h-2 rounded-full mt-3 overflow-hidden">
            <div 
              className="bg-primary h-full rounded-full transition-all" 
              style={{ width: `${completionRate}%` }}
            />
          </div>
        </div>

        {/* 총 몰입 시간 */}
        <div className="rounded-xl border bg-card p-6 shadow-sm flex flex-col justify-between">
          <div className="text-sm font-medium text-muted-foreground">총 몰입 시간</div>
          <div className="mt-2 text-2xl font-bold whitespace-nowrap">
            {formatDuration(data.totalDurationMinutes)}
          </div>
          <p className="text-xs text-muted-foreground mt-2">
            어제 하루 동안 집중한 시간입니다.
          </p>
        </div>
      </div>

      {/* 상세 일정 리스트 */}
      <div className="space-y-4">
        <h3 className="text-lg font-bold flex items-center gap-2">
          <CheckCircle2 className="w-5 h-5 text-primary" />
          완료한 일정 분석
        </h3>
        <div className="grid gap-3">
          {data.taskDurations.map((task, index) => {
            const diff = task.actualDurationMinutes - task.plannedDurationMinutes
            const isOvertime = diff > 0
            const isSaveTime = diff < 0
            
            return (
              <div key={index} className="flex flex-col sm:flex-row sm:items-center justify-between p-4 rounded-lg border bg-card gap-4">
                <div className="flex items-center gap-3">
                  <div className="h-2 w-2 rounded-full bg-primary/50" />
                  <span className="font-medium">{task.title}</span>
                </div>
                
                <div className="flex items-center gap-4 text-sm">
                  <div className="flex items-center gap-2 text-muted-foreground bg-secondary/50 px-3 py-1 rounded-md">
                    <span>계획 {formatDuration(task.plannedDurationMinutes)}</span>
                    <ArrowRight className="w-3 h-3" />
                    <span className={`font-semibold ${isOvertime ? 'text-red-500' : isSaveTime ? 'text-blue-500' : 'text-foreground'}`}>
                      실제 {formatDuration(task.actualDurationMinutes)}
                    </span>
                  </div>
                  
                  {diff !== 0 && (
                    <span className={`text-xs px-2 py-1 rounded-full font-medium whitespace-nowrap
                      ${isOvertime ? 'bg-red-100 text-red-600' : 'bg-blue-100 text-blue-600'}`}>
                      {isOvertime ? `+${diff}분 초과` : `${diff}분 단축`}
                    </span>
                  )}
                </div>
              </div>
            )
          })}
          {data.taskDurations.length === 0 && (
            <div className="text-center py-8 text-muted-foreground text-sm">
              완료된 일정이 없습니다.
            </div>
          )}
        </div>
      </div>

      {/* 못 마친 일정 */}
      {data.unfinishedTodoTitles.length > 0 && (
        <div className="space-y-4 pt-4 border-t">
          <h3 className="text-lg font-bold flex items-center gap-2 text-muted-foreground">
            <AlertCircle className="w-5 h-5" />
            못 마친 일정
          </h3>
          <div className="grid gap-2">
            {data.unfinishedTodoTitles.map((title, index) => (
              <div key={index} className="flex items-center gap-3 p-3 rounded-lg border border-dashed bg-muted/30 text-muted-foreground">
                <XCircle className="h-5 w-5 flex-shrink-0" />
                <span>{title}</span>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  )
}