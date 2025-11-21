// src/hooks/useTodaySchedules.ts
import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { getTodaySchedules } from "@/api/todayApi";
import { useToast } from "@/hooks/use-toast";

export function useTodaySchedules() {
  const { toast } = useToast();

  // 날짜 구하기 로직
  const today = new Date().toLocaleDateString("en-CA", {
    timeZone: "Asia/Seoul",
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });

  const { data: schedules = [], refetch } = useQuery({
    queryKey: ["todaySchedules", today],
    queryFn: () => getTodaySchedules(today),
  });

  const [selectedScheduleId, setSelectedScheduleId] = useState<number | null>(
    null,
  );

  // 실행 중인 일정 찾기
  const runningSchedule = schedules.find(
    (s) => s.realStartTime !== null && !s.finished,
  );

  const selectedSchedule = schedules.find((s) => s.id === selectedScheduleId);

  // 선택 핸들러
  const selectSchedule = (id: number) => {
    if (runningSchedule && runningSchedule.id !== id) {
      toast({
        title: "🚫 이동 불가",
        description: "현재 진행 중인 일정이 있습니다. 먼저 완료해주세요.",
        variant: "destructive",
      });
      return;
    }
    setSelectedScheduleId(id);
  };

  return {
    todayDate: today,
    schedules,
    selectedScheduleId,
    selectedSchedule,
    runningSchedule,
    selectSchedule,
    refetch,
  };
}
