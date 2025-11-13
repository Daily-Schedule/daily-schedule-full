package daily_schedule.daily_schedule_be.dto;

import lombok.Getter;
import java.time.LocalDateTime;

// API 요청 본문(Request Body)을 받을 클래스
@Getter
public class TomorrowRequestDto {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String content;
}