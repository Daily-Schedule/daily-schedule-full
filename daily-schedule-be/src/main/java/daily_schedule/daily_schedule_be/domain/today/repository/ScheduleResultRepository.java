package daily_schedule.daily_schedule_be.domain.today.repository;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 'ScheduleResult' 일정 결과 엔티티를 관리하는 엔티티 관리자
 * <p>
 * 일정 시작/종료 API가 수정(UPDATE)를 완료한 후, 저장하라는 명령을 내릴 대상
 */
public interface ScheduleResultRepository extends JpaRepository<ScheduleResult, Long> {
    // JpaRepository를 상속받는 것만으로도
    // 1. .save(방) : '방'을 저장하거나 '수정(UPDATE)'
    // 2. .findById(id) : '방'을 ID로 찾아옴
    // 3. .findAll() : 모든 '방'을 찾아옴
    // ...등의 기본 기능들이 '자동'으로 탑재
}