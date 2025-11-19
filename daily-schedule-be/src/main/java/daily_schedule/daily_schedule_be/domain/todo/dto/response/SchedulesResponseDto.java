package daily_schedule.daily_schedule_be.domain.todo.dto.response;

import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

// API 응답(Response)으로 보낼 클래스
@Getter
public class SchedulesResponseDto {
    private Long id;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
    private Long scheduleResultId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Todo Entity를 받아서 DTO로 변환하는 생성자
    public SchedulesResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        // User 객체에서 ID를 꺼내오도록 변경
        this.userId = schedule.getUser().getId();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        // ScheduleResult 객체에서 ID를 꺼내오도록 변경
        if (schedule.getScheduleResult() != null) {
            this.scheduleResultId = schedule.getScheduleResult().getId();
        }
        this.content = schedule.getContent();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}