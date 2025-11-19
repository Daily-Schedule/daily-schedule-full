package daily_schedule.daily_schedule_be.domain.todo.dto.request;

import lombok.Getter;
import java.time.LocalDateTime;

// API 요청 본문(Request Body)을 받을 클래스
@Getter
public class SchedulesRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
}