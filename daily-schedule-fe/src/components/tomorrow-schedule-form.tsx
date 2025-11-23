"use client";

import type React from "react";

import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Plus, Loader2, Save } from "lucide-react";
import {
  postTodoSchedule,
  patchTodoSchedule,
  type TodoScheduleDTO,
} from "@/api/tomorrowApi";

interface TomorrowScheduleFormProps {
  // 내일 날짜
  targetDate: string;
  // 등록 성공 시 목록 새로고침을 위한 콜백
  onSuccess: () => void;
  // 수정 모드일 경우 데이터를 전달받음
  initialData?: TodoScheduleDTO;
  // 수정 모드일 때 모달을 닫기 위한 콜백
  onCancel?: () => void;
}

export function TomorrowScheduleForm({
  targetDate,
  onSuccess,
  initialData,
  onCancel,
}: TomorrowScheduleFormProps) {
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [content, setContent] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // 수정 모드일 경우 초기값 세팅
  useEffect(() => {
    if (initialData) {
      // ISO String (2023-11-24T09:00:00) -> Time String (09:00) 변환
      const extractTime = (iso: string) => {
        const date = new Date(iso);
        const hours = date.getHours().toString().padStart(2, "0");
        const minutes = date.getMinutes().toString().padStart(2, "0");
        return `${hours}:${minutes}`;
      };

      setStartTime(extractTime(initialData.startTime));
      setEndTime(extractTime(initialData.endTime));
      setContent(initialData.content);
    }
  }, [initialData]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!startTime || !endTime || !content) return;

    setIsSubmitting(true);

    try {
      // 입력받은 시간(HH:mm)을 API 스펙에 맞는 포맷(ISO String 등)으로 변환
      const formattedStartTime = `${targetDate}T${startTime}:00`;
      const formattedEndTime = `${targetDate}T${endTime}:00`;

      const payload = {
        startTime: formattedStartTime,
        endTime: formattedEndTime,
        content: content,
      };

      if (initialData) {
        // [수정 모드]
        await patchTodoSchedule(initialData.id, payload);
        alert("일정이 수정되었습니다.");
      } else {
        // [등록 모드]
        await postTodoSchedule(payload);
      }

      // 폼 초기화 (등록 모드일 때만)
      if (!initialData) {
        setStartTime("");
        setEndTime("");
        setContent("");
      }
      // 목록 갱신
      onSuccess();
      // 모달 닫기
      if (onCancel) onCancel();
    } catch (error) {
      console.error("일정 등록 실패:", error);
      alert("일정 등록 중 오류가 발생했습니다.");
    } finally {
      setIsSubmitting(false);
    }
  };
  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="start-time">시작 시간</Label>
          <Input
            id="start-time"
            type="time"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            required
          />
        </div>
        <div className="space-y-2">
          <Label htmlFor="end-time">종료 시간</Label>
          <Input
            id="end-time"
            type="time"
            value={endTime}
            onChange={(e) => setEndTime(e.target.value)}
            required
          />
        </div>
      </div>
      <div className="space-y-2">
        <Label htmlFor="content">일정 내용</Label>
        <Input
          id="content"
          type="text"
          placeholder="내일 할 일을 입력하세요"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
        />
      </div>
      <div className="flex gap-2">
        {/* 취소 버튼 (수정 모드일 때만 표시) */}
        {onCancel && (
          <Button
            type="button"
            variant="outline"
            className="flex-1"
            onClick={onCancel}
          >
            취소
          </Button>
        )}
        <Button type="submit" className="flex-1" disabled={isSubmitting}>
          {isSubmitting ? (
            <Loader2 className="h-4 w-4 mr-2 animate-spin" />
          ) : initialData ? (
            // 수정 아이콘
            <Save className="h-4 w-4 mr-2" /> 
          ) : (
            // 등록 아이콘
            <Plus className="h-4 w-4 mr-2" /> 
          )}
          {initialData ? "수정 저장" : "일정 추가"}
        </Button>
      </div>
    </form>
  );
}
