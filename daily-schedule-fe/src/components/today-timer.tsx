import { useState, useEffect } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Play, Square } from "lucide-react"; // Square 아이콘(정지) 사용
import {
  startSchedule,
  endSchedule,
  type TodayScheduleDTO,
} from "@/api/todayApi";
import { useToast } from "@/hooks/use-toast"; // 토스트 메시지 사용 (선택사항)

interface TodayTimerProps {
  selectedSchedule?: TodayScheduleDTO; // 선택된 일정이 없을 수도 있음
  onScheduleEnd: () => void; // 종료 후 목록 새로고침 콜백
}

export function TodayTimer({
  selectedSchedule,
  onScheduleEnd,
}: TodayTimerProps) {
  const { toast } = useToast();
  const [seconds, setSeconds] = useState(0);
  const [isRunning, setIsRunning] = useState(false);

  // 타이머 로직 (1초마다 증가)
  useEffect(() => {
    let interval: ReturnType<typeof setInterval> | null = null;
    if (isRunning) {
      interval = setInterval(() => setSeconds((prev) => prev + 1), 1000);
    }
    return () => {
      if (interval) clearInterval(interval);
    };
  }, [isRunning]);

  // 선택된 일정이 바뀌면 타이머 리셋
  // useEffect(() => {
  //   setSeconds(0);
  //   setIsRunning(false);
  // }, [selectedSchedule?.id]);

  const formatTime = (totalSeconds: number) => {
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const secs = totalSeconds % 60;
    return `${String(hours).padStart(2, "0")}:${String(minutes).padStart(
      2,
      "0",
    )}:${String(secs).padStart(2, "0")}`;
  };

  // ▶️ 시작 핸들러
  const handleStart = async () => {
    if (!selectedSchedule) return toast({ title: "일정을 먼저 선택해주세요!" });

    try {
      await startSchedule(selectedSchedule.id); // API 호출
      setIsRunning(true);
      toast({ title: "집중 시작!", description: "타이머가 돌아갑니다." });
    } catch (error) {
      console.error(error);
      toast({ title: "오류 발생", variant: "destructive" });
    }
  };

  // ⏹️ 종료(완료) 핸들러
  const handleEnd = async () => {
    if (!selectedSchedule) return;

    // 종료 확인 (실수 방지)
    if (
      !confirm(
        "일정을 종료하시겠습니까? 종료 후에는 시간을 더 추가할 수 없습니다.",
      )
    )
      return;

    try {
      await endSchedule(selectedSchedule.id); // API 호출
      setIsRunning(false);
      onScheduleEnd(); // 부모(페이지)에게 알려서 리스트 새로고침!
      toast({
        title: "고생하셨습니다!",
        description: "일정이 완료 처리되었습니다.",
      });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Card>
      <CardContent className="pt-6">
        <div className="text-center space-y-4">
          {/* 현재 선택된 일정 제목 보여주기 */}
          <h3 className="text-lg font-medium text-muted-foreground min-h-[28px]">
            {selectedSchedule ? selectedSchedule.content : "일정을 선택하세요"}
          </h3>

          <div className="text-6xl font-mono font-bold tracking-wider">
            {formatTime(seconds)}
          </div>

          <div className="flex gap-3 justify-center">
            {!isRunning ? (
              <Button
                size="lg"
                onClick={handleStart}
                className="w-32"
                disabled={!selectedSchedule || selectedSchedule.finished}
              >
                <Play className="h-5 w-5 mr-2" /> 시작
              </Button>
            ) : (
              // 실행 중일 때는 '일시정지'가 아니라 '종료' 버튼을 노출
              <Button
                size="lg"
                variant="destructive"
                onClick={handleEnd}
                className="w-32"
              >
                <Square className="h-5 w-5 mr-2 fill-current" /> 완료
              </Button>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
