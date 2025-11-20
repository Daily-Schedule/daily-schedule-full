import { useMemo } from "react";
import { Link } from "react-router-dom";
import { TomorrowScheduleForm } from "@/components/tomorrow-schedule-form";
import { TomorrowScheduleList } from "@/components/tomorrow-schedule-list";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function TomorrowPage() {
  const tomorrowDateString = useMemo(() => {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    return tomorrow.toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "long",
      day: "numeric",
      weekday: "long",
    });
  }, []);

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
            <p className="text-muted-foreground">{tomorrowDateString}</p>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>일정 등록</CardTitle>
            </CardHeader>
            <CardContent>
              <TomorrowScheduleForm />
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>내일의 일정</CardTitle>
            </CardHeader>
            <CardContent>
              <TomorrowScheduleList />
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
