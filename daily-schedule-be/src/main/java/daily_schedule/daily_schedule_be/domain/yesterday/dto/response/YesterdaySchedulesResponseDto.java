package daily_schedule.daily_schedule_be.domain.yesterday.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YesterdaySchedulesResponseDto {
    // 첫 일정 시작 시각 비교 (양수면 지각, 음수면 이른 시작)
    private long startDelayMinutes;
    // 전체 일정 소요 시간
    private long totalDurationMinutes;
    // 각 일정 별 소요 시간 리스트
    private List<TaskDurationDto> taskDurations;
    // 못 마친 일정 리스트
    private List<String> unfinishedTodoTitles;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TaskDurationDto {
        private String title;
        // 계획 소요 시간
        private long plannedDurationMinutes;
        // 실제 소요 시간
        private long actualDurationMinutes;
    }
}
