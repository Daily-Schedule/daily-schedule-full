package daily_schedule.daily_schedule_be.domain.today.service;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.today.repository.ScheduleResultRepository;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import daily_schedule.daily_schedule_be.domain.todo.repository.SchedulesRepository;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * '오늘 일정' 기능의 핵심 비즈니스 로직을 구현하는 Service
 * <p>
 * {@link SchedulesRepository}, {@link ScheduleResultRepository}를 통해 DB 작업을 수행
 *
 * @Service : 이 클래스가 '서비스' 계층의 컴포넌트임을 스프링에게 알림
 * @RequiredArgsConstructor : final 필드(Repository)에 대한 생성자를 자동으로 생성
 */
@Service
@RequiredArgsConstructor
public class TodayScheduleService {

    // 일정 계획을 관리하는 레포지토리
    // 통합된 레포지토리로 변경 (종근님 레포지토리)
    private final SchedulesRepository schedulesRepository;
    // 일정 결과를 관리하는 레포지토리
    // 추후 API는 이 레포지토리를 통해 '결과'를 '저장(UPDATE)' 함
    private final ScheduleResultRepository scheduleResultRepository;


    /**
     * '오늘 일정 목록 조회' 로직
     *
     * @param user (입력) 조회할 사용자 (현재는 임시로 null)
     * @param date (입력) 조회할 날짜 (예: "2025-11-16")
     * @return {@link Schedule} (일정 계획) 엔티티의 목록
     */
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByDate(User user, LocalDate date) {
        // 날짜(LocalDate)를 그날의 시작 시간과 끝 시간으로 변환
        // 00:00:00 ~ 23:59:59
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // Repository에 새로 만든 쿼리 메소드 호출
        // return todayScheduleRepository.findByUserAndStartTimeBetween(user,
        // startOfDay, endOfDay);
        return schedulesRepository.findAllByUserAndStartTimeBetween(user,
                startOfDay, endOfDay);
    }

    /**
     * '특정 일정 시작' 로직
     *
     * @param scheduleId (입력) 'Controller가 전달해준 "시작할 일정의 ID"
     * @Transactional : 이 작업(메소드)은 하나의 '묶음(트랜잭션)'
     * 중간에 실패하면 모든 변경사항이 '롤백(취소)'되어야 함을 보장
     */
    @Transactional
    public void startSchedule(Long scheduleId) {
        // scheduleId로 '오늘 일정(TodaySchedule)'을 찾는다
        // domain의 @Id 필드명: scheduleResultId
        // TodaySchedule -> Schedule
        Schedule schedule = schedulesRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정 없음"));

        // 조회된 일정에서 '결과 ID'를 꺼낸다
        // domain의 필드명: scheduleResultDailyResultId
        ScheduleResult result = schedule.getScheduleResult();   // 필드명 변경
        if (result == null)
            throw new IllegalStateException("일정에 결과 ID가 연결되지 않았습니다.");
        // 이미 시작 시간이 기록되어 있다면 예외 처리 or 무시
        if (result.getRealStartTime() != null) {
            throw new IllegalStateException("이미 시작된 일정입니다.");
        }

        // '결과' 엔티티의 'realStartTime' 필드에 '현재 시간'을 기록(Setter)
        result.setRealStartTime(LocalDateTime.now().withNano(0));

        // 레포지토리에게 변경된 'result' 객체를 저장(UPDATE)
        // scheduleResultRepository.save(result);
        // 변경 감지(Dirty Checking)로 인해 save 호출 안해도 되지만 명시적으로 유지
        scheduleResultRepository.save(result);
    }

    /**
     * '특정 일정 종료' 로직
     *
     * @param scheduleId (입력) 종료할 일정의 ID
     */
    @Transactional
    public void endSchedule(Long scheduleId) {
        // '계획' 엔티티를 탐색
//        TodaySchedule schedule = todayScheduleRepository.findById(scheduleId)
//                .orElseThrow(() -> new IllegalArgumentException("일정 없음"));
        // [변경] TodaySchedule -> Schedule
        Schedule schedule = schedulesRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정 없음"));

        // '계획'에 연결된 '결과' 엔티티를 탐색
        // ScheduleResult result = schedule.getScheduleResultId();
        // 필드명 변경
        ScheduleResult result = schedule.getScheduleResult();
        if (result == null)
            throw new IllegalStateException("일정에 결과 객체가 연결되지 " + "않았습니다.");

        // '결과' 엔티티의 '실제 종료 시간' 필드에 '현재 시간'을 기록
        result.setRealEndTime(LocalDateTime.now().withNano(0));
        // '결과' 엔티티의 '완료 여부' 필드를 'true'로 기록
        result.setFinished(true);

        // '일정 결과' 레포지토리에게 변경사항 저장(UPDATE) 명령
        scheduleResultRepository.save(result);
    }
}
