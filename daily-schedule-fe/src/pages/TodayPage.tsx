import { useState } from "react";
import { useQuery } from "@tanstack/react-query"; // React Query 추가
import { Link } from "react-router-dom";
import { TodayTimer } from "@/components/today-timer";
import { TodayScheduleList } from "@/components/today-schedule-list";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getTodaySchedules } from "@/api/todayApi"; // API 함수 임포트
import { useToast } from "@/hooks/use-toast";

export default function TodayPage() {
  const { toast } = useToast(); // [추가] 훅을 통해 toast 함수 가져오기

  // 1. 한국 시간(KST) 기준으로 YYYY-MM-DD 형식 구하기
  // const today = new Date().toISOString().split("T")[0]; // [기존 코드 삭제]
  const today = new Date().toLocaleDateString("en-CA", {
    // en-CA는 YYYY-MM-DD 형식을 반환함
    timeZone: "Asia/Seoul",
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });

  // 2. 서버에서 일정 데이터 가져오기 (React Query 사용)
  const {
    data: schedules = [], // 데이터가 없으면 빈 배열
    refetch, // 데이터를 다시 불러오는 함수 (타이머 종료 후 목록 갱신용)
  } = useQuery({
    queryKey: ["todaySchedules", today],
    queryFn: () => getTodaySchedules(today),
  });

  // 3. 현재 선택된 일정 ID 상태 관리
  const [selectedScheduleId, setSelectedScheduleId] = useState<number | null>(
    null,
  );

  // [1] 현재 '진행 중'인 일정 찾기 (시작은 했는데, 아직 안 끝난 일정)
  const runningSchedule = schedules.find(
    (s) => s.realStartTime !== null && !s.finished,
  );

  // [2] 일정 선택 핸들러 (차단 로직 포함)
  const handleSelectSchedule = (id: number) => {
    // 진행 중인 일정이 존재하고(Running) && 내가 클릭한게 그 일정이 아니라면(Diff)
    if (runningSchedule && runningSchedule.id !== id) {
      toast({
        title: "🚫 이동 불가",
        description: "현재 진행 중인 일정이 있습니다. 먼저 완료해주세요.",
        variant: "destructive",
      });
      return; // 강제 종료 (선택 변경 안 함)
    }

    // 통과되면 선택
    setSelectedScheduleId(id);
  };

  // 선택된 일정 객체 찾기 (타이머에 제목 등을 보여주기 위해)
  // const selectedSchedule = schedules.find((s) => s.id === selectedScheduleId);

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b">
        <div className="container mx-auto px-4 h-16 flex items-center justify-between">
          <h1 className="text-xl font-bold">일정 관리</h1>
          <nav className="flex gap-6">
            <Link
              to="/yesterday"
              className="text-sm text-muted-foreground hover:text-foreground transition-colors"
            >
              어제
            </Link>
            <Link to="/today" className="text-sm font-medium">
              오늘
            </Link>
            <Link
              to="/tomorrow"
              className="text-sm text-muted-foreground hover:text-foreground transition-colors"
            >
              내일
            </Link>
          </nav>
        </div>
      </header>

      <main className="container mx-auto px-4 py-8 max-w-2xl">
        <div className="space-y-6">
          <div>
            <h2 className="text-3xl font-bold mb-2">오늘</h2>
            <p className="text-muted-foreground">
              {new Date().toLocaleDateString("ko-KR", {
                year: "numeric",
                month: "long",
                day: "numeric",
                weekday: "long",
              })}
            </p>
          </div>

          {/* 타이머에 선택된 일정 정보와 목록 갱신 함수 전달 */}
          <TodayTimer
            key={selectedScheduleId}
            // selectedSchedule={selectedSchedule}
            selectedSchedule={schedules.find(
              (s) => s.id === selectedScheduleId,
            )}
            onScheduleEnd={refetch}
          />

          <Card>
            <CardHeader>
              <CardTitle>오늘의 일정</CardTitle>
            </CardHeader>
            <CardContent>
              {/* 리스트에 데이터와 선택 핸들러 전달 */}
              <TodayScheduleList
                schedules={schedules}
                selectedScheduleId={selectedScheduleId}
                onSelect={handleSelectSchedule}
              />
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
