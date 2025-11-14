package daily_schedule.daily_schedule_be.repository;

import daily_schedule.daily_schedule_be.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<관리할 Entity, Entity의 ID 타입>
// 인터페이스만 만들어두면 Spring Data JPA가 알아서 구현체를 만듦
// save(), findById(), findAll() 등 기본 메서드는 이미 구현되어 있음
public interface SchedulesRepository extends JpaRepository<Schedule, Long> {

}