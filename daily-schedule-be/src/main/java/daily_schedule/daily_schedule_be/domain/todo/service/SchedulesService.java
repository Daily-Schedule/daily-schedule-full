package daily_schedule.daily_schedule_be.domain.todo.service;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.today.repository.ScheduleResultRepository;
import daily_schedule.daily_schedule_be.domain.todo.dto.request.SchedulesRequestDto;
import daily_schedule.daily_schedule_be.domain.todo.dto.response.SchedulesResponseDto;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import daily_schedule.daily_schedule_be.domain.todo.repository.SchedulesRepository;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
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
    private final ScheduleResultRepository scheduleResultRepository;
    private final UserRepository userRepository;

    // 이 메서드 전체가 하나의 트랜잭션으로 동작
    // CREATE
    @Transactional
    public SchedulesResponseDto createSchedule(String userId, SchedulesRequestDto requestDto) {

        // ID로 User 엔티티를 조회 (없으면 에러 발생)
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 결과 객체 생성
        ScheduleResult newResult = ScheduleResult.createNewResult();
        // (명시적으로 저장)
        ScheduleResult savedResult = scheduleResultRepository.save(newResult);
//        Long resultId = savedResult.getId();
        // 요청받은 DTO와 자동 생성한 userId, resultId로 Entity를 생성
//        Schedule schedule = new Schedule(requestDto, userId, resultId);
        Schedule schedule = Schedule.builder().content(requestDto.getContent())
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .user(user)             // [핵심] User 객체 연결
                .scheduleResult(newResult) // [핵심] Result 객체 연결
                .build();

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

        // ID로 User 엔티티 조회

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // User 객체로 조회하는걸로 변경
        List<Schedule> schedules = schedulesRepository.findAllByUserAndStartTimeBetween(
                user, startOfDay, endOfDay);

        return schedules.stream().map(SchedulesResponseDto::new)
                .collect(Collectors.toList());
    }

    // UPDATE
    @Transactional
    public SchedulesResponseDto updateSchedule(Long id, String userId,
                                               SchedulesRequestDto requestDto) {
        Schedule schedule = schedulesRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 스케줄이 없습니다. id=" + id));

        // 유저 권한 확인
        // 기존: schedule.getUserId().equals(userId)
        // 변경: schedule.getUser().getId()로 User 객체에서 ID를 꺼내와서 비교
        if (!schedule.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }


        // update 메서드 호출 방식 변경
        // 기존: schedule.update(requestDto);
        // 변경: Entity에 정의한 update 메서드는 필드들을 개별적으로 받도록 되어 있음
        schedule.update(requestDto.getContent(), requestDto.getStartTime(),
                requestDto.getEndTime());

        return new SchedulesResponseDto(schedule);
    }

    // DELETE
    @Transactional
    public void deleteSchedule(Long id, String userId) {
        Schedule schedule = schedulesRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 스케줄이 없습니다. id=" + id));

        // 유저 권한 확인 (위와 동일)
        if (!schedule.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        // 연결된 결과 객체 삭제
        // 기존: schedule.getScheduleResultId() -> Long 반환
        // 변경: schedule.getScheduleResult().getId() -> 객체에서 ID 꺼내기

        // 참고: Schedule 엔티티에 cascade = CascadeType.ALL이 걸려있다면
        // scheduleResultRepository.deleteById(...)를 명시하지 않아도
        // schedulesRepository.delete(schedule)만 하면 결과 객체도 같이 삭제
        // 하지만 명시적으로 지우고 싶다면 아래처럼 ID를 꺼내야 함
        Long resultId = schedule.getScheduleResult().getId();
        scheduleResultRepository.deleteById(resultId);

        schedulesRepository.delete(schedule);
    }
}