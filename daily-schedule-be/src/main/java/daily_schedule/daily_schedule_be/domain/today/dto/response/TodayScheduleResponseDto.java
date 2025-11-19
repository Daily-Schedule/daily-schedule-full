package daily_schedule.daily_schedule_be.domain.today.dto.response;

import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodayScheduleResponseDto {
    private Long id;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isFinished; // 결과 객체에서 뽑아올 정보

    // 엔티티를 DTO로 바꿔주는 마법사 메서드
    // 파라미터 타입 TodaySchedule -> Schedule
    public static TodayScheduleResponseDto from(Schedule entity) {
        boolean finished = false;
        // 결과 객체가 연결되어 있다면 그 안의 값을 가져옴
//        if (entity.getScheduleResultId() != null) {
//            finished = entity.getScheduleResultId().isFinished();
//        }
        // getScheduleResultId() -> getScheduleResult()
        if (entity.getScheduleResult() != null) {
            finished = entity.getScheduleResult().isFinished();
        }

        return TodayScheduleResponseDto.builder().id(entity.getId())
                .content(entity.getContent()).startTime(entity.getStartTime())
                .endTime(entity.getEndTime()).isFinished(finished).build();
    }
}