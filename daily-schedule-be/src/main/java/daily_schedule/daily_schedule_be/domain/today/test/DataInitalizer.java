package daily_schedule.daily_schedule_be.domain.today.test;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.today.entity.TodaySchedule;
import daily_schedule.daily_schedule_be.domain.today.repository.ScheduleResultRepository;
import daily_schedule.daily_schedule_be.domain.today.repository.TodayScheduleRepository;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitalizer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TodayScheduleRepository todayScheduleRepository;
    private final ScheduleResultRepository scheduleResultRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. 테스트 유저 생성 (없으면 생성)
        User user = userRepository.findById("test-user").orElseGet(
                () -> userRepository.save(User.builder().id("test-user")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("테스트유저").build()));

        // 2. 오늘 일정 데이터 생성 (일정이 하나도 없을 때만 생성)
        if (todayScheduleRepository.count() == 0) {
            ScheduleResult result = new ScheduleResult();
            result.setFinished(false);
            scheduleResultRepository.save(result);

            TodaySchedule schedule = new TodaySchedule();
            schedule.setContent("포스트맨 테스트 성공하기");
            schedule.setStartTime(LocalDateTime.now().minusHours(1)); // 1시간 전
            schedule.setEndTime(LocalDateTime.now().plusHours(1));   // 1시간 후
            schedule.setUser(user); // 유저 연결
            schedule.setScheduleResultId(result); // 결과 연결

            todayScheduleRepository.save(schedule);
            System.out.println("=========== 테스트 데이터 생성 완료 ===========");
        }
    }
}