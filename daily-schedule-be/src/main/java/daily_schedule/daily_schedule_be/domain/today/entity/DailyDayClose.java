package daily_schedule.daily_schedule_be.domain.today.entity;

import daily_schedule.daily_schedule_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "daily_day_close", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "close_date"}) // 하루에 한 번만 마감 가능
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyDayClose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "close_date", nullable = false)
    private LocalDate closeDate;
}