package daily_schedule.daily_schedule_be.domain.today.repository;

import daily_schedule.daily_schedule_be.domain.today.entity.TodaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 'TodaySchedule' 일정 상세를 관리하는 엔티티 관리자
 * <p>
 * JpaRepository<관리할 '엔티티 이름', 엔티티의 PK 타입>
 */
public interface TodayScheduleRepository extends JpaRepository<TodaySchedule, Long> {
    /**
     * '오늘 일정 목록 조회' API를 위한 '검색' 기능
     * <p>
     * Spring Data JPA가 이 메소드 이름(findBy...)을 분석하여
     * "SELECT * FROM schedule_detail WHERE user_id = ? AND start_time BETWEEN ? AND ?"
     * 와 같은 SQL 쿼리를 '자동'으로 생성
     *
     * @param user  (조건 1) 검색할 사용자 엔티티
     * @param start (조건 2) 검색할 '시작' 시간 (예: 00:00:00)
     * @param end   (조건 3) 검색할 '종료' 시간 (예: 23:59:59)
     * @return {@link TodaySchedule} 엔티티의 목록
     */
    List<TodaySchedule> findByUserAndStartTimeBetween(
            daily_schedule.daily_schedule_be.domain.user.entity.User user,
            LocalDateTime start, LocalDateTime end);
}
