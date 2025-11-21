import { Link } from "react-router-dom";
import { TodayTimer } from "@/components/today-timer";
import { TodayScheduleList } from "@/components/today-schedule-list";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useTodaySchedules } from "@/hooks/useTodaySchedules";

export default function TodayPage() {
  // 커스텀 훅 사용
  const {
    schedules,
    selectedScheduleId,
    selectedSchedule,
    selectSchedule,
    refetch,
  } = useTodaySchedules();

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
            selectedSchedule={selectedSchedule}
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
                onSelect={selectSchedule}
              />
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
