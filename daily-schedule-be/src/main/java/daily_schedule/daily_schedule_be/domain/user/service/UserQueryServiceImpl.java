package daily_schedule.daily_schedule_be.domain.user.service;

import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public abstract class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    public boolean isUserExist(String userId) {
        return userRepository.existsById(userId);
    }
}
