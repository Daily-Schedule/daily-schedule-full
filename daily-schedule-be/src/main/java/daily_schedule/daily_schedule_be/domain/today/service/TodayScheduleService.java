package daily_schedule.daily_schedule_be.domain.today.service;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.today.entity.TodaySchedule;
import daily_schedule.daily_schedule_be.domain.today.repository.ScheduleResultRepository;
import daily_schedule.daily_schedule_be.domain.today.repository.TodayScheduleRepository;
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
 * {@link TodayScheduleRepository}, {@link ScheduleResultRepository}를 통해 DB 작업을 수행
 *
 * @Service : 이 클래스가 '서비스' 계층의 컴포넌트임을 스프링에게 알림
 * @RequiredArgsConstructor : final 필드(Repository)에 대한 생성자를 자동으로 생성
 */
@Service
@RequiredArgsConstructor
public class TodayScheduleService {

    // 일정 계획을 관리하는 레포지토리
    private final TodayScheduleRepository todayScheduleRepository;
    // 일정 결과를 관리하는 레포지토리
    // 추후 API는 이 레포지토리를 통해 '결과'를 '저장(UPDATE)' 함
    private final ScheduleResultRepository scheduleResultRepository;


    /**
     * '오늘 일정 목록 조회' 로직
     *
     * @param user (입력) 조회할 사용자 (현재는 임시로 null)
     * @param date (입력) 조회할 날짜 (예: "2025-11-16")
     * @return {@link TodaySchedule} (일정 계획) 엔티티의 목록
     */
    @Transactional(readOnly = true)
    public List<TodaySchedule> getSchedulesByDate(User user, LocalDate date) {
        // 날짜(LocalDate)를 그날의 시작 시간과 끝 시간으로 변환
        // 00:00:00 ~ 23:59:59
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // Repository에 새로 만든 쿼리 메소드 호출
        return todayScheduleRepository.findByUserAndStartTimeBetween(user,
                startOfDay, endOfDay);
        // 현재는 빈 배열 반환 -> 추후 '일정 목록'이 반한됨
    }

}
