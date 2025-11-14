package daily_schedule.daily_schedule_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// @EnableJpaAuditing: @CreatedDate 기능을 활성화하는 어노테이션
@EnableJpaAuditing
public class DailyScheduleBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyScheduleBeApplication.class, args);
	}

}
