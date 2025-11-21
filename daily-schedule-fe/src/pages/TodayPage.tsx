import { Link } from "react-router-dom";
import { TodayTimer } from "@/components/today-timer";
import { TodayScheduleList } from "@/components/today-schedule-list";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useTodaySchedules } from "@/hooks/useTodaySchedules";
import { Button } from "@/components/ui/button";

export default function TodayPage() {
  // 커스텀 훅 사용
  const {
    schedules,
    selectedScheduleId,
    selectedSchedule,
    selectSchedule,
    refetch,
    isDayFinished,
    handleFinishDay,
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

          {/* [하단 고정 버튼 영역] */}
          <div className="fixed bottom-8 left-0 right-0 flex justify-center px-4 z-50">
            <div className="max-w-2xl w-full">
              {!isDayFinished ? (
                // [마감 전] 빨간색 종료 버튼 노출
                <Button
                  size="lg"
                  variant="destructive"
                  className="w-full shadow-xl text-lg h-14 font-bold tracking-wide"
                  onClick={handleFinishDay}
                >
                  오늘 하루 마무리하기
                </Button>
              ) : (
                // [마감 후] 안내 메시지 박스 노출
                <div className="bg-card border-2 border-primary/20 rounded-xl p-6 text-center shadow-2xl animate-in slide-in-from-bottom-10 fade-in duration-500">
                  <h3 className="text-xl font-bold text-primary mb-1">
                    🌙 오늘 하루가 마감되었습니다
                  </h3>
                  <p className="text-muted-foreground text-sm">
                    수고하셨습니다! 내일이 되어야 분석 결과를 확인할 수
                    있습니다.
                  </p>
                </div>
              )}
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}
