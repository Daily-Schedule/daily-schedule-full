// src/hooks/useTodaySchedules.ts
import { useState, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  getTodaySchedules,
  finishToday,
  checkIsDayFinished,
} from "@/api/todayApi";
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

  // [추가] 하루 마감 여부 상태
  const [isDayFinished, setIsDayFinished] = useState(false);

  // [추가] 페이지 접속 시 마감 여부 확인 (새로고침 대비)
  useEffect(() => {
    checkIsDayFinished(today).then((finished) => setIsDayFinished(finished));
  }, [today]);

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
    // 마감된 상태면 일정 선택도 못하게 막음
    if (isDayFinished) return;

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

  // 하루 마감 버튼 핸들러
  const handleFinishDay = async () => {
    // 1. 진행 중인 타이머가 있으면 마감 불가
    if (runningSchedule) {
      toast({
        title: "진행 중인 일정이 있습니다!",
        description: "먼저 일정을 완료해주세요.",
        variant: "destructive",
      });
      return;
    }

    // 2. 사용자 확인
    if (
      !confirm(
        "오늘 하루를 마무리하시겠습니까? 완료 후에는 더 이상 수정할 수 없습니다.",
      )
    )
      return;

    try {
      // 3. API 호출
      await finishToday(today);
      // 4. 상태 업데이트 (화면 잠금)
      setIsDayFinished(true);
      setSelectedScheduleId(null); // 선택 해제
      toast({
        title: "수고하셨습니다! 🎉",
        description: "오늘 하루가 마감되었습니다.",
      });
    } catch (error) {
      console.error(error);
      toast({
        title: "오류 발생",
        description: "잠시 후 다시 시도해주세요.",
        variant: "destructive",
      });
    }
  };

  return {
    todayDate: today,
    schedules,
    selectedScheduleId,
    selectedSchedule,
    runningSchedule,
    selectSchedule,
    refetch,
    isDayFinished,
    handleFinishDay,
  };
}
