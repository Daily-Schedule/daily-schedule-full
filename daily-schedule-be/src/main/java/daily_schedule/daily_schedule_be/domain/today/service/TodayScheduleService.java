package daily_schedule.daily_schedule_be.domain.today.service;

import daily_schedule.daily_schedule_be.domain.today.entity.DailyDayClose;
import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.today.repository.DailyDayCloseRepository;
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
 * [오늘 일정 서비스]
 * <p>
 * 컨트롤러의 요청을 받아 실제 DB 작업을 수행하는 핵심 로직 계층
 * <p>
 * {@link SchedulesRepository}, {@link ScheduleResultRepository}를 통해 DB 작업을 수행
 */
@Service    // 이 클래스가 '서비스' 계층의 컴포넌트임을 스프링에게 알림
@RequiredArgsConstructor // final 필드(Repository)에 대한 생성자를 자동으로 생성
public class TodayScheduleService {

    // 일정 계획을 관리하는 레포지토리
    // 통합된 레포지토리로 변경 (종근님 레포지토리)
    private final SchedulesRepository schedulesRepository;
    // 일정 결과를 관리하는 레포지토리
    // 추후 API는 이 레포지토리를 통해 '결과'를 '저장(UPDATE)' 함
    private final ScheduleResultRepository scheduleResultRepository;
    // 마감 관리 레포지토리
    private final DailyDayCloseRepository dailyDayCloseRepository;


    /**
     * [조회] 특정 날짜의 일정 목록 가져오기
     * <p>
     * 날짜(LocalDate)를 받아서 그 날의 00:00:00 ~ 23:59:59 사이의 데이터를 조회함
     *
     * @param user (입력) 조회할 사용자
     * @param date (입력) 조회할 날짜 (예: "2025-11-16")
     * @return {@link Schedule} (일정 계획) 엔티티의 목록
     */
    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByDate(User user, LocalDate date) {
        // 2025-11-22 -> 2025-11-22 00:00:00 ~ 2025-11-22 23:59:59.999999
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // DB에서 해당 범위 내의 일정 검색
        return schedulesRepository.findAllByUserAndStartTimeBetween(user,
                startOfDay, endOfDay);
    }

    /**
     * [수정] 일정 시작 (타이머 START)
     * <p></p>
     * 실제 시작 시간(realStartTime)을 현재 시간으로 기록
     *
     */
    // 이 작업(메소드)은 하나의 '묶음(트랜잭션)' 중간에 실패하면 모든 변경사항이 '롤백(취소)'되어야 함을 보장
    @Transactional
    public void startSchedule(Long scheduleId) {
        // scheduleId로 '오늘 일정(TodaySchedule)'을 찾는다
        Schedule schedule = schedulesRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정 없음"));

        // 조회된 일정에서 '결과 ID'를 꺼낸다
        ScheduleResult result = schedule.getScheduleResult();
        if (result == null)
            throw new IllegalStateException("일정에 결과 ID가 연결되지 않았습니다.");
        // 이미 시작한 적 있는지 체크 (중복 시작 방지)
        if (result.getRealStartTime() != null) {
            throw new IllegalStateException("이미 시작된 일정입니다.");
        }

        // '결과' 엔티티의 'realStartTime' 필드에 '현재 시간'을 기록(Setter)
        result.setRealStartTime(LocalDateTime.now().withNano(0));

        // 레포지토리에게 변경된 'result' 객체를 저장(UPDATE)
        // 변경 감지(Dirty Checking)로 인해 save 호출 안해도 되지만 명시적으로 유지
        scheduleResultRepository.save(result);
    }

    /**
     * [수정] 일정 종료 (타이머 END)
     * <p>
     * 실제 종료 시간(realEndTime) 기록 및 완료 상태(finished) 변경
     *
     * @param scheduleId (입력) 종료할 일정의 ID
     */
    @Transactional
    public void endSchedule(Long scheduleId) {
        // '계획' 엔티티를 탐색
        Schedule schedule = schedulesRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정 없음"));

        // '계획'에 연결된 '결과' 엔티티를 탐색
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

    /**
     * [저장] 오늘 하루 마감하기
     * <p>
     * 마감 테이블(DailyDayClose)에 기록을 남겨서, 프론트엔드가 수정 못하게 막는 기준이 됨
     *
     * @param user
     * @param date
     */
    @Transactional
    public void finishDay(User user, LocalDate date) {
        // 이미 마감했는지 확인 (중복 저장 방지)
        if (dailyDayCloseRepository.existsByUserAndCloseDate(user, date)) {
            return;
        }

        DailyDayClose dayClose = DailyDayClose.builder().user(user)
                .closeDate(date).build();

        dailyDayCloseRepository.save(dayClose);
    }

    /**
     * [조회] 오늘 하루 마감 여부 확인
     *
     * @return true면 마감된 상태, false면 아직 진행 중
     */
    @Transactional(readOnly = true)
    public boolean isDayFinished(User user, LocalDate date) {
        return dailyDayCloseRepository.existsByUserAndCloseDate(user, date);
    }
}
