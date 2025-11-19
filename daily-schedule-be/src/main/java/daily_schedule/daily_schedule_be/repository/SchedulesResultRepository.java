package daily_schedule.daily_schedule_be.repository;

import daily_schedule.daily_schedule_be.domain.ScheduleResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulesResultRepository extends JpaRepository<ScheduleResult, Long>{
}
