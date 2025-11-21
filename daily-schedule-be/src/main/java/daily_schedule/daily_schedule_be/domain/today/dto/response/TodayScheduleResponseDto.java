package daily_schedule.daily_schedule_be.domain.today.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TodayScheduleResponseDto {
    private Long id;
    private String content;
    // 프론트엔드로 보낼 때 "년-월-일 시:분:초" 형식으로 고정 (T 문자 제거)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endTime;
    // 실제 시작 시간 (이 값이 존재하면 타이머가 돌고 있다는 증거)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime realStartTime;

    private boolean isFinished; // 결과 객체에서 뽑아올 정보

    private long plannedDuration; // 계획 순공 시간 (분 단위)
    private long realDuration;  // 실제 순공 시간 (분 단위)

    // 엔티티를 DTO로 바꿔주는 마법사 메서드
    // 파라미터 타입 TodaySchedule -> Schedule
    public static TodayScheduleResponseDto from(Schedule entity) {
        boolean finished = false;
        long realDurationValue = 0; // 초기값:0
        LocalDateTime realStartTimeValue = null; // 초기값 null

        long plannedDurationValue = entity.calculatePlannedDuration();

        // 실제 수행 시간 계산
        if (entity.getScheduleResult() != null) {
            ScheduleResult result = entity.getScheduleResult();
            finished = result.isFinished();
            // 결과 객체에서 실제 시작 시간 꺼내기
            realStartTimeValue = result.getRealStartTime();

            realDurationValue = result.calculateRealDuration();
        }

        return TodayScheduleResponseDto.builder().id(entity.getId())
                .content(entity.getContent()).startTime(entity.getStartTime())
                .endTime(entity.getEndTime()).isFinished(finished)
                // 계산된 값 주입
                .plannedDuration(plannedDurationValue)
                .realDuration(realDurationValue)
                .realStartTime(realStartTimeValue).build();

    }
}