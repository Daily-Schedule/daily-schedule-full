"use client";

import { Checkbox } from "@/components/ui/checkbox";
import type { TodayScheduleDTO } from "@/api/todayApi";
import { cn } from "@/lib/utils"; // 스타일 조건부 적용을 위해

// Props 타입 정의
interface TodayScheduleListProps {
  schedules: TodayScheduleDTO[];
  selectedScheduleId: number | null;
  onSelect: (id: number) => void;
}

export function TodayScheduleList({
  schedules,
  selectedScheduleId,
  onSelect,
}: TodayScheduleListProps) {
  // (참고) 체크박스 클릭 시 완료 처리는 별도 API가 없으므로
  // 현재 로직상으로는 타이머 종료(End) 시에만 완료가 됨.
  // 따라서 여기서는 '보여주기' 용도로 사용하거나, 필요시 체크 해제 API 등을 추가해야 함.

  return (
    <div className="space-y-3">
      {schedules.length === 0 ? (
        <p className="text-center text-muted-foreground py-8">
          등록된 일정이 없습니다
        </p>
      ) : (
        schedules.map((schedule) => {
          // ✨ 핵심 로직: 실제 시간이 계획 시간보다 적으면 완료 불가
          const isTimeEnough =
            schedule.realDuration >= schedule.plannedDuration;
          // 현재 선택된 항목인지 확인
          const isSelected = selectedScheduleId === schedule.id;

          return (
            <div
              key={schedule.id}
              onClick={() => onSelect(schedule.id)} // 클릭 시 선택
              className={cn(
                "flex items-center gap-3 p-3 rounded-lg border transition-colors cursor-pointer",
                isSelected
                  ? "border-primary bg-primary/5"
                  : "bg-card hover:bg-accent/50", // 선택된 항목 강조
                schedule.finished ? "opacity-60" : "",
              )}
            >
              <Checkbox
                checked={schedule.finished}
                disabled={!isTimeEnough} // 🚫 시간이 부족하면 체크박스 비활성화 (클릭 불가)
                id={`schedule-${schedule.id}`}
                // 체크박스 클릭 이벤트 전파 방지 (부모 div 클릭 방지)
                onClick={(e) => e.stopPropagation()}
              />

              <label
                htmlFor={`schedule-${schedule.id}`}
                className="flex-1 flex flex-col cursor-pointer"
              >
                <div className="flex items-center gap-2">
                  <span className="text-sm font-mono text-muted-foreground min-w-[50px]">
                    {/* 시:분(4자리)가 나오도록 substring 범위 변경 */}
                    {`${schedule.startTime.substring(
                      11,
                      17,
                    )} ~ ${schedule.endTime.substring(11, 17)}`}
                    {/* {schedule.startTime.substring(0, 5)}{" "} */}
                  </span>
                  <span
                    className={
                      schedule.finished
                        ? "line-through text-muted-foreground"
                        : ""
                    }
                  >
                    {schedule.content}
                  </span>
                </div>
                {/* ⏱️ 시간 비교 정보 표시 (사용자 피드백) */}
                <span
                  className={cn(
                    "text-xs mt-1 ml-[58px]",
                    isTimeEnough ? "text-blue-500" : "text-red-400",
                  )}
                >
                  {schedule.realDuration}분 / {schedule.plannedDuration}분 달성
                </span>
              </label>
            </div>
          );
        })
      )}
    </div>
  );
}
