package daily_schedule.daily_schedule_be.domain.user.service;

import daily_schedule.daily_schedule_be.domain.user.dto.response.UserResponseDTO;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserQueryService {
    User getUserByUserId(String userId);
    boolean isUserExist(String userId);

    UserResponseDTO.UserInfo getUserInfo(String userId);
}
