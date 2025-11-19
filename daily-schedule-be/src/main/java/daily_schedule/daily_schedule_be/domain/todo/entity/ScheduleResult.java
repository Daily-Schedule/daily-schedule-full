package daily_schedule.daily_schedule_be.domain.todo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Schedule Entity에서 참조할 ID
    private Long id;
    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isFinished = false;


    public static ScheduleResult createNewResult() {
        return new ScheduleResult();
    }
}