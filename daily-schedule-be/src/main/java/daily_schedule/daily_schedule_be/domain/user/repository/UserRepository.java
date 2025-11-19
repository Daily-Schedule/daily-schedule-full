package daily_schedule.daily_schedule_be.domain.user.repository;

import daily_schedule.daily_schedule_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String id);
    Optional<User> findById(String id);

}
