import { useEffect, useMemo, useState, useCallback } from "react";
import { Link } from "react-router-dom";
import { TomorrowScheduleForm } from "@/components/tomorrow-schedule-form";
import { TomorrowScheduleList } from "@/components/tomorrow-schedule-list";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { getTodoSchedules, type TodoScheduleDTO } from "@/api/tomorrowApi";

export default function TomorrowPage() {
  const [schedules, setSchedules] = useState<TodoScheduleDTO[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  const { displayDate, apiDate } = useMemo(() => {
    const d = new Date();
    d.setDate(d.getDate() + 1);

    // 화면 표시용
    const display = d.toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "long",
      day: "numeric",
      weekday: "long",
    });

    // API 전송용 (YYYY-MM-DD) - 한국 시간 기준
    const offset = d.getTimezoneOffset() * 60000;
    const dateOffset = new Date(d.getTime() - offset);
    const api = dateOffset.toISOString().split("T")[0];

    return { displayDate: display, apiDate: api };
  }, []);

  // 일정 목록 가져오기 함수
  const fetchSchedules = useCallback(async () => {
    try {
      setIsLoading(true);
      const data = await getTodoSchedules(apiDate);
      // 데이터가 없을 경우 빈 배열 처리
      setSchedules(data || []);
    } catch (error) {
      console.error("스케줄 로딩 실패", error);
    } finally {
      setIsLoading(false);
    }
  }, [apiDate]);

  // 페이지 진입 시 데이터 로딩
  useEffect(() => {
    fetchSchedules();
  }, [fetchSchedules]);

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
            <Link
              to="/today"
              className="text-sm text-muted-foreground hover:text-foreground transition-colors"
            >
              오늘
            </Link>
            <Link to="/tomorrow" className="text-sm font-medium">
              내일
            </Link>
          </nav>
        </div>
      </header>

      <main className="container mx-auto px-4 py-8 max-w-2xl">
        <div className="space-y-6">
          <div>
            <h2 className="text-3xl font-bold mb-2">내일</h2>
            <p className="text-muted-foreground">{displayDate}</p>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>일정 등록</CardTitle>
            </CardHeader>
            <CardContent>
              <TomorrowScheduleForm
                targetDate={apiDate}
                onSuccess={fetchSchedules}
              />
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>내일의 일정</CardTitle>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="text-center py-8 text-muted-foreground">
                  불러오는 중...
                </div>
              ) : (
                /* List에 데이터와 삭제 성공 시 갱신 함수 전달 */
                <TomorrowScheduleList
                  schedules={schedules}
                  targetDate={apiDate}        
                  onDeleteSuccess={fetchSchedules}
                  onUpdateSuccess={fetchSchedules} 
                />
              )}
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
