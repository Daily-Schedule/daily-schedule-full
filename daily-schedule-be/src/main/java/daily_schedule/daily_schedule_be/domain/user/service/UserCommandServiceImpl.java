package daily_schedule.daily_schedule_be.domain.user.service;

import daily_schedule.daily_schedule_be.domain.user.dto.request.UserRequestDTO;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
import daily_schedule.daily_schedule_be.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void register(UserRequestDTO.Signup request){
        if(userRepository.existsById(request.getId())){
            throw new IllegalArgumentException("이미 사용 중인 ID 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder().id(request.getId()).password(encodedPassword).nickname(request.nickname()).build();
        userRepository.save(user);
    }

    @Override
    public String login(UserRequestDTO.Login request){
        User user = userRepository.findById(request.id())
                .orElseThrow(()-> new IllegalArgumentException("ID 또는 비밀번호가 일치하지 않습니다."));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("ID 또는 비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.createToken(user.getId());

    }

}
