"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { X, Pencil } from "lucide-react";
import { deleteTodoSchedule, type TodoScheduleDTO } from "@/api/tomorrowApi";
import { TomorrowScheduleForm } from "@/components/tomorrow-schedule-form";

interface TomorrowScheduleListProps {
  schedules: TodoScheduleDTO[];
  // 수정 시 필요한 날짜 정보
  targetDate: string;
  // 삭제 후 새로고침 콜백
  onDeleteSuccess: () => void;
  // 수정 완료 후 새로고침 콜백
  onUpdateSuccess: () => void;
}

const formatTime = (isoString: string) => {
  if (!isoString) return "";
  const date = new Date(isoString);
  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");
  return `${hours}:${minutes}`;
};

export function TomorrowScheduleList({
  schedules,
  targetDate,
  onDeleteSuccess,
  onUpdateSuccess,
}: TomorrowScheduleListProps) {
  const [editingSchedule, setEditingSchedule] =
    useState<TodoScheduleDTO | null>(null);

  const handleDelete = async (e: React.MouseEvent, id: number) => {
    // 부모 div의 클릭 이벤트(수정 모달 열기) 방지
    e.stopPropagation();
    if (confirm("정말 이 일정을 삭제하시겠습니까?")) {
      try {
        await deleteTodoSchedule(id);
        // 목록 갱신
        onDeleteSuccess();
      } catch (error) {
        console.error("삭제 실패:", error);
        alert("삭제 중 오류가 발생했습니다.");
      }
    }
  };

  return (
    <>
      <div className="space-y-3">
        {!schedules || schedules.length === 0 ? (
          <p className="text-center text-muted-foreground py-8">
            등록된 일정이 없습니다
          </p>
        ) : (
          schedules.map((schedule) => (
            <div
              key={schedule.id}
              onClick={() => setEditingSchedule(schedule)}
              className="flex items-center gap-3 p-3 rounded-lg border bg-card hover:bg-accent/50 transition-colors group cursor-pointer"
            >
              <div className="flex-1 flex items-center gap-3">
                <span className="text-sm font-mono text-muted-foreground whitespace-nowrap">
                  {formatTime(schedule.startTime)} -{" "}
                  {formatTime(schedule.endTime)}
                </span>
                <span className="font-medium">{schedule.content}</span>
              </div>
              <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={(e) => {
                    e.stopPropagation();
                    setEditingSchedule(schedule);
                  }}
                >
                  <Pencil className="h-4 w-4 text-blue-500" />
                </Button>

                <Button
                  variant="ghost"
                  size="icon"
                  onClick={(e) => handleDelete(e, schedule.id)}
                >
                  <X className="h-4 w-4 text-red-500" />
                </Button>
              </div>
            </div>
          ))
        )}
      </div>
      {/* 커스텀 모달 구현 (Dialog 컴포넌트 없이 Tailwind로 직접 구현) */}
      {editingSchedule && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80 p-4">
          <div
            className="bg-background border w-full max-w-md rounded-lg p-6 shadow-lg relative"
            onClick={(e) => e.stopPropagation()} // 모달 내부 클릭 시 닫힘 방지
          >
            {/* 모달 헤더 */}
            <div className="flex flex-col space-y-1.5 text-center sm:text-left mb-4">
              <h2 className="text-lg font-semibold leading-none tracking-tight">
                일정 수정
              </h2>
              <p className="text-sm text-muted-foreground">
                선택한 일정의 시간과 내용을 수정합니다.
              </p>
            </div>

            {/* 폼 컴포넌트 */}
            <TomorrowScheduleForm
              targetDate={targetDate}
              initialData={editingSchedule}
              onSuccess={() => {
                onUpdateSuccess();
                setEditingSchedule(null);
              }}
              onCancel={() => setEditingSchedule(null)}
            />
          </div>
        </div>
      )}
    </>
  );
}
