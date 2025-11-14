package daily_schedule.daily_schedule_be.domain.today.repository;

import daily_schedule.daily_schedule_be.domain.today.entity.TodaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

// DB 연결
public interface TodayScheduleRepository extends JpaRepository<TodaySchedule, Long> {
    List<TodaySchedule> findByUserAndStartTimeBetween(
            daily_schedule.daily_schedule_be.domain.user.entity.User user,
            LocalDateTime start, LocalDateTime end);
}
