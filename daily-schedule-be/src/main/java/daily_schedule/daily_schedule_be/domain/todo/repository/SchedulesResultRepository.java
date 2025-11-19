package daily_schedule.daily_schedule_be.domain.todo.repository;

import daily_schedule.daily_schedule_be.domain.todo.entity.ScheduleResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulesResultRepository extends JpaRepository<ScheduleResult, Long>{
}
