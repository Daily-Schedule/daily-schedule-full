package daily_schedule.daily_schedule_be.domain.today.repository;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import org.springframework.data.jpa.repository.JpaRepository;

// DB 연결
public interface ScheduleResultRepository extends JpaRepository<ScheduleResult, Long> {
    // JpaRepository를 상속받아, findById(), save() 등 기본 기능은 완성
}