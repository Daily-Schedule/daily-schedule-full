package daily_schedule.daily_schedule_be.domain.todo.service;

import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import daily_schedule.daily_schedule_be.domain.todo.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.todo.dto.request.SchedulesRequestDto;
import daily_schedule.daily_schedule_be.domain.todo.dto.response.SchedulesResponseDto;
import daily_schedule.daily_schedule_be.domain.todo.repository.SchedulesRepository;
import daily_schedule.daily_schedule_be.domain.todo.repository.SchedulesResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

// Spring에게 서비스 로직 담당임을 알리기 위한 어노테이션
@Service
// final이 붙은 필드의 생성자를 자동으로 만들어주는 의존성 주입 (DI)
@RequiredArgsConstructor
public class SchedulesService {
    private final SchedulesRepository schedulesRepository;
    private final SchedulesResultRepository scheduleResultRepository;

    // 이 메서드 전체가 하나의 트랜잭션으로 동작
    // CREATE
    @Transactional
    public SchedulesResponseDto createSchedule(String userId, SchedulesRequestDto requestDto) {

        ScheduleResult newResult = ScheduleResult.createNewResult();
        ScheduleResult savedResult = scheduleResultRepository.save(newResult);
        Long resultId = savedResult.getId();
        // 요청받은 DTO와 자동 생성한 userId, resultId로 Entity를 생성
        Schedule schedule = new Schedule(requestDto, userId, resultId);

        // Repository를 이용해 DB에 저장
        Schedule savedSchedule = schedulesRepository.save(schedule);

        // 저장된 Entity를 Response DTO로 변환하여 반환
        return new SchedulesResponseDto(savedSchedule);
    }

    // READ
    @Transactional(readOnly = true)
    public List<SchedulesResponseDto> readSchedule(String userId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        // Repository 쿼리 메서드 변경
        List<Schedule> schedules = schedulesRepository.findAllByUserIdAndStartTimeBetween(userId, startOfDay, endOfDay);

        return schedules.stream()
                .map(SchedulesResponseDto::new)
                .collect(Collectors.toList());
    }

    // UPDATE
    @Transactional
    public SchedulesResponseDto updateSchedule(Long id, String userId, SchedulesRequestDto requestDto) {
        Schedule schedule = schedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 없습니다. id=" + id));

        if (!schedule.getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // Entity의 update 메서드 호출
        schedule.update(requestDto);

        return new SchedulesResponseDto(schedule);
    }

    // DELETE
    @Transactional
    public void deleteSchedule(Long id, String userId) {
        Schedule schedule = schedulesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 스케줄이 없습니다. id=" + id));

        if (!schedule.getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        // 스케줄을 삭제할 때 연결된 결과 객체도 함께 삭제
        scheduleResultRepository.deleteById(schedule.getScheduleResultId());
        schedulesRepository.delete(schedule);
    }
}