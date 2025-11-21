import { api } from "@/lib/axios";

// 백엔드 TodayScheduleResponseDto와 매칭되는 타입 정의
export interface TodayScheduleDTO {
  id: number;
  content: string;
  startTime: string; // 백엔드의 LocalDateTime은 문자열로 넘어옴
  endTime: string;
  realStartTime: string | null;
  finished: boolean; // Java의 boolean isFinished -> JSON에선 finished로 변환됨 (Jackson 기본 동작)
  plannedDuration: number;
  realDuration: number;
}

/**
 * 1. 오늘 일정 목록 조회
 * GET /api/today-schedules?date=YYYY-MM-DD
 */
export const getTodaySchedules = async (date: string) => {
  const response = await api.get<TodayScheduleDTO[]>(`/today-schedules`, {
    params: { date }, // 자동으로 ?date=... 쿼리 파라미터 생성
  });
  return response.data;
};

/**
 * 2. 일정 시작 (타이머 시작)
 * PATCH /api/today-schedules/{scheduleId}/start
 */
export const startSchedule = async (scheduleId: number) => {
  const response = await api.patch<string>(
    `/today-schedules/${scheduleId}/start`,
  );
  return response.data;
};

/**
 * 3. 일정 종료 (타이머 종료 및 완료 처리)
 * PATCH /api/today-schedules/{scheduleId}/end
 */
export const endSchedule = async (scheduleId: number) => {
  const response = await api.patch<string>(
    `/today-schedules/${scheduleId}/end`,
  );
  return response.data;
};
