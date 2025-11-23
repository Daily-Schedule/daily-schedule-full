import { axiosInstance } from "./common/axiosInstance";

// API 응답 데이터 타입 정의 (Service에서 넘겨주는 DTO와 일치)
export interface TodoScheduleDTO {
  id: number;
  userId: string;
  content: string;
  startTime: string;
  endTime: string;
  scheduleResultId: number;
  createdAt: string;
  updateAt: string;
}

export interface ScheduleData {
  startTime: string;
  endTime: string;
  content: string;
}

/**
 * 일정 목록 조회
 * GET /api/schedules?date=YYYY-MM-DD
 */
export const getTodoSchedules = async (date: string) => {
  try {
    const response = await axiosInstance.get<TodoScheduleDTO[]>("/api/schedules", {
      params: { date },
    });
    return response.data;
  } catch (error) {
    console.log("일정 목록 가져오기 실패", error);
  }
};

/**
 * 일정 등록
 * POST /api/schedules
 */
export const postTodoSchedule = async (data: ScheduleData) => {
  try {
    const response = await axiosInstance.post("/api/schedules", data);
    return response.data;
  } catch (error) {
    console.log("일정 등록 실패", error);
  }
};

/**
 * 일정 수정
 * PATCH /api/schedules?id={scheduleId}
 */
export const patchTodoSchedule = async (scheduleId: number, data: ScheduleData) => {
  try {
    const response = await axiosInstance.patch<string>(
      `/api/schedules?id=${scheduleId}`,
      data
    );
    return response.data;
  } catch (error) {
    console.log("일정 수정 실패", error);
    throw error;
  }
};

/**
 * 일정 삭제
 * DELETE /api/schedules?id={scheduleId}
 */
export const deleteTodoSchedule = async (scheduleId: number) => {
  try {
    const response = await axiosInstance.delete<string>(
      `/api/schedules?id=${scheduleId}`
    );
    return response.data;
  } catch (error) {
    console.log("일정 삭제 실패", error);
  }
};
