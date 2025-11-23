import { axiosInstance } from "./common/axiosInstance";
// API 응답 데이터 타입 정의 (Service에서 넘겨주는 DTO와 일치)
export interface TaskDurationDto {
  title: string;
  plannedDurationMinutes: number;
  actualDurationMinutes: number;
}

export interface YesterdayResponseDto {
  startDelayMinutes: number;
  totalDurationMinutes: number;
  taskDurations: TaskDurationDto[];
  unfinishedTodoTitles: string[];
}
/**
 * 어제 대시보드 통계 조회
 * GET /api/yesterday?date=YYYY-MM-DD
 */
export const getYesterdayStatistics = async (date: string) => {
  const response = await axiosInstance.get<YesterdayResponseDto>(`/api/yesterday`, {
    // ?date=2025-11-21 형태로 변환
    params: { date },
  });
  return response.data;
};