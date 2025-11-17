package daily_schedule.daily_schedule_be.domain.user.service;

import daily_schedule.daily_schedule_be.domain.user.dto.response.UserResponseDTO;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    public boolean isUserExist(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

    }
    @Override
    public UserResponseDTO.UserInfo getUserInfo(String userId) {
        User user = getUserByUserId(userId);
        return UserResponseDTO.UserInfo.builder()
                .name(user.getNickname())
                .build();
    }
}
