import { Link } from "react-router-dom";
import { TodayTimer } from "@/components/today-timer";
import { TodayScheduleList } from "@/components/today-schedule-list";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

export default function TodayPage() {
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

          <TodayTimer />

          <Card>
            <CardHeader>
              <CardTitle>오늘의 일정</CardTitle>
            </CardHeader>
            <CardContent>
              <TodayScheduleList />
            </CardContent>
          </Card>

          <div className="pt-8">
            <Button
              variant="destructive"
              size="lg"
              className="w-full h-14 text-lg"
            >
              하루 종료
            </Button>
          </div>
        </div>
      </main>
    </div>
  );
}
