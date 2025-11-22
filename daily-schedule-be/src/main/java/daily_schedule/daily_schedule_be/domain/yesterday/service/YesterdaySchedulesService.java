package daily_schedule.daily_schedule_be.domain.yesterday.service;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import daily_schedule.daily_schedule_be.domain.todo.repository.SchedulesRepository; // Repository 경로 확인 필요
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
import daily_schedule.daily_schedule_be.domain.yesterday.dto.response.YesterdaySchedulesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class YesterdaySchedulesService {

    private final SchedulesRepository schedulesRepository;
    private final UserRepository userRepository;

    public YesterdaySchedulesResponseDto getDailyStatistics(String userId, String date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        // 해당 날짜의 모든 Schedule 조회
        List<Schedule> dailyTodos = schedulesRepository.findAllByUserAndStartTimeBetween(user, startOfDay, endOfDay);

        // 데이터를 시간순으로 정렬 (계획 시작 시간 기준)
        dailyTodos.sort(Comparator.comparing(Schedule::getStartTime));

        // 계산을 위한 변수 초기화
        long startDelayMinutes = 0;
        long totalDurationMinutes = 0;
        List<YesterdaySchedulesResponseDto.TaskDurationDto> taskDurations = new ArrayList<>();
        List<String> unfinishedTodoTitles = new ArrayList<>();

        // 데이터가 하나도 없을 경우 빈 객체 반환
        if (dailyTodos.isEmpty()) {
            return new YesterdaySchedulesResponseDto(0, 0, taskDurations, unfinishedTodoTitles);
        }

        // 첫 일정 시작 시각 비교 (계획 vs 실제)
        Schedule firstTodo = dailyTodos.getFirst();
        ScheduleResult firstResult = firstTodo.getScheduleResult();

        if (firstResult != null && firstResult.getRealStartTime() != null) {
            Duration diff = Duration.between(firstTodo.getStartTime(), firstResult.getRealStartTime());
            // 양수: 지각, 음수: 일찍 시작
            startDelayMinutes = diff.toMinutes();
        }

        // 전체 일정 (마지막 종료 - 첫 일정 종료)
        // 실제 종료 시간이 존재하는 일정만 필터링
        List<Schedule> finishedTodos = dailyTodos.stream()
                .filter(t -> t.getScheduleResult() != null && t.getScheduleResult().getRealEndTime() != null)
                .sorted(Comparator.comparing(t -> t.getScheduleResult().getRealEndTime()))
                .toList();

        if (!finishedTodos.isEmpty()) {
            LocalDateTime firstRealEndTime = finishedTodos.getFirst().getScheduleResult().getRealEndTime();
            LocalDateTime lastRealEndTime = finishedTodos.getLast().getScheduleResult().getRealEndTime();

            totalDurationMinutes = Duration.between(firstRealEndTime, lastRealEndTime).toMinutes();
        }

        // 개별 일정 소요 시간 및 미완료 일정 리스트
        for (Schedule todo : dailyTodos) {
            ScheduleResult result = todo.getScheduleResult();

            // 미완료 일정 체크 (실제 시작 시각이 없거나 isFinished가 false인 경우)
            if (result == null || result.getRealStartTime() == null) {
                unfinishedTodoTitles.add(todo.getContent());
                continue;
            }

            // 개별 일정 시간 계산
            long plannedDuration = Duration.between(todo.getStartTime(), todo.getEndTime()).toMinutes();

            long actualDuration = 0;
            if (result.getRealEndTime() != null) {
                actualDuration = Duration.between(result.getRealStartTime(), result.getRealEndTime()).toMinutes();
            }

            taskDurations.add(new YesterdaySchedulesResponseDto.TaskDurationDto(
                    todo.getContent(),
                    plannedDuration,
                    actualDuration
            ));
        }

        return YesterdaySchedulesResponseDto.builder()
                .startDelayMinutes(startDelayMinutes)
                .totalDurationMinutes(totalDurationMinutes)
                .taskDurations(taskDurations)
                .unfinishedTodoTitles(unfinishedTodoTitles)
                .build();
    }
}
