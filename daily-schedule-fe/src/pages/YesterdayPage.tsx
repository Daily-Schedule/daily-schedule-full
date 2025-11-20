import { Link } from "react-router-dom";
import { YesterdayAnalysis } from "@/components/yesterday-analysis";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useMemo } from "react";

export default function YesterdayPage() {
  const yesterdayDateString = useMemo(() => {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    return yesterday.toLocaleDateString("ko-KR", {
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
            <Link to="/yesterday" className="text-sm font-medium">
              어제
            </Link>
            <Link
              to="/today"
              className="text-sm text-muted-foreground hover:text-foreground transition-colors"
            >
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
            <h2 className="text-3xl font-bold mb-2">어제</h2>
            <p className="text-muted-foreground">{yesterdayDateString}</p>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>결과 분석</CardTitle>
            </CardHeader>
            <CardContent>
              <YesterdayAnalysis />
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
