package daily_schedule.daily_schedule_be.domain.today.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


/**
 * [프론트엔드 응답용 DTO]
 * <p>
 * Schedule(계획)과 ScheduleResult(결과) 엔티티의 정보를 합쳐서
 * <p>
 * 프론트엔드가 사용하기 편한 형태로 가공하여 전달함
 */
@Getter
@Builder
public class TodayScheduleResponseDto {
    private Long id;
    private String content; // 할 일 내용
    // @JsonFormat: 날짜를 "2025-11-22 14:30:00" 문자열 형식으로 예쁘게 포맷팅해서 내보냄
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime realStartTime; // 실제 시작 시간 (이 값이 존재하면 타이머가 돌고 있다는 증거)

    private boolean isFinished; // 완료 여부 (체크박스 체크 여부)

    private long plannedDuration; // 계획 순공 시간 (분 단위)
    private long realDuration;  // 실제 순공 시간 (분 단위)

    /**
     * [Entity -> DTO 변환 메서드]
     * <p>
     * DB에서 가져온 Schedule(계획) 엔티티를 이 DTO로 변환하는 로직
     * <p>
     * 계획 정보와 결과(Result) 정보를 합치는 과정
     */
    public static TodayScheduleResponseDto from(Schedule entity) {
        boolean finished = false;
        long realDurationValue = 0; // 초기값:0
        LocalDateTime realStartTimeValue = null; // 초기값 null

        // 계획된 시간 계산 (종료시간 - 시작시간)
        long plannedDurationValue = entity.calculatePlannedDuration();

        // 결과(ScheduleResult) 데이터가 있다면 정보를 채워넣음
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