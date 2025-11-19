package daily_schedule.daily_schedule_be.domain.user.service;

import daily_schedule.daily_schedule_be.domain.user.dto.request.UserRequestDTO;

public interface UserCommandService {
    void register(UserRequestDTO.Signup signupRequest);

    String login(UserRequestDTO.Login loginRequest);
}
