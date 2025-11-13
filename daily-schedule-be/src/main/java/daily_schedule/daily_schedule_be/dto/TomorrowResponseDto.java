package daily_schedule.daily_schedule_be.dto;

import daily_schedule.daily_schedule_be.domain.Todo;
import lombok.Getter;
import java.time.LocalDateTime;

// API 응답(Response)으로 보낼 클래스
@Getter
public class TomorrowResponseDto {
    private String id;
    private String author;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String content;
    private LocalDateTime createdAt;

    // Todo Entity를 받아서 DTO로 변환하는 생성자
    public TomorrowResponseDto(Todo todo) {
        this.id = todo.getId();
        this.author = todo.getAuthor();
        this.startAt = todo.getStartAt();
        this.endAt = todo.getEndAt();
        this.content = todo.getContent();
        this.createdAt = todo.getCreateAt();
    }
}