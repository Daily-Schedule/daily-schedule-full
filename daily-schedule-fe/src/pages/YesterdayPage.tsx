import { Link } from "react-router-dom";
import { YesterdayAnalysis } from "@/components/yesterday-analysis";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useMemo, useState, useEffect } from "react";
import { getYesterdayStatistics, type YesterdayResponseDto } from "@/api/yesterdayApi";

export default function YesterdayPage() {
  const [data, setData] = useState<YesterdayResponseDto | undefined>(undefined);
  const [error, setError] = useState<boolean>(false);

  const { displayDate, apiDate } = useMemo(() => {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);

    const display = yesterday.toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "long",
      day: "numeric",
      weekday: "long",
    });

    const offset = yesterday.getTimezoneOffset() * 60000;
    const dateOffset = new Date(yesterday.getTime() - offset);
    const api = dateOffset.toISOString().split('T')[0];

    return { displayDate: display, apiDate: api };
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const result = await getYesterdayStatistics(apiDate);
        setData(result);
      } catch (err) {
        console.error("Failed to fetch yesterday stats:", err);
        setError(true);
      }
    };

    fetchData();
  }, [apiDate]);

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
            <p className="text-muted-foreground">{displayDate}</p>
          </div>

          <Card>
            <CardHeader>
              <CardTitle>결과 분석</CardTitle>
            </CardHeader>
            <CardContent>
              {error ? (
                <div className="text-center py-10 text-red-500">
                  데이터를 불러오는데 실패했습니다.
                </div>
              ) : (
                /* 데이터가 없으면(undefined) 자식 컴포넌트에서 로딩 표시가 뜸 */
                <YesterdayAnalysis data={data} />
              )}
            </CardContent>
          </Card>
        </div>
      </main>
    </div>
  );
}
