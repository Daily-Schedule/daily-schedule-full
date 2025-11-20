package daily_schedule.daily_schedule_be.domain.today.dto.response;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Builder
public class TodayScheduleResponseDto {
    private Long id;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isFinished; // 결과 객체에서 뽑아올 정보

    private long plannedDuration; // 계획 순공 시간 (분 단위)
    private long realDuration;  // 실제 순공 시간 (분 단위)

    // 엔티티를 DTO로 바꿔주는 마법사 메서드
    // 파라미터 타입 TodaySchedule -> Schedule
    public static TodayScheduleResponseDto from(Schedule entity) {
        boolean finished = false;
        long realDurationValue = 0; // 초기값:0

        long plannedDurationValue = Duration.between(entity.getStartTime(),
                entity.getEndTime()).toMinutes();

        // 결과 객체가 연결되어 있다면 그 안의 값을 가져옴
//        if (entity.getScheduleResult() != null) {
//            finished = entity.getScheduleResult().isFinished();
//        }
        // 2. 실제 수행 시간 계산
        if (entity.getScheduleResult() != null) {
            ScheduleResult result = entity.getScheduleResult();
            finished = result.isFinished();

            // 시작 시간과 종료 시간이 모두 존재할 때만 계산 (종료 버튼을 눌러야 시간이 확정되므로)
            if (result.getRealStartTime() != null && result.getRealEndTime() != null) {
                realDurationValue = Duration.between(result.getRealStartTime(),
                        result.getRealEndTime()).toMinutes();
            }
        }

        return TodayScheduleResponseDto.builder().id(entity.getId())
                .content(entity.getContent()).startTime(entity.getStartTime())
                .endTime(entity.getEndTime()).isFinished(finished)
                // 계산된 값 주입
                .plannedDuration(plannedDurationValue)
                .realDuration(realDurationValue).build();
    }
}