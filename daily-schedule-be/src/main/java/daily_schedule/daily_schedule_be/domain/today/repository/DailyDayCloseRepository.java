package daily_schedule.daily_schedule_be.domain.today.repository;

import daily_schedule.daily_schedule_be.domain.today.entity.DailyDayClose;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyDayCloseRepository extends JpaRepository<DailyDayClose, Long> {
    // 해당 유저가 해당 날짜를 마감했는지 확인
    boolean existsByUserAndCloseDate(User user, LocalDate closeDate);
}