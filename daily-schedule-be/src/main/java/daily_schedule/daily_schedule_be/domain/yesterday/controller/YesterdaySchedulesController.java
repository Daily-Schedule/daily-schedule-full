package daily_schedule.daily_schedule_be.domain.yesterday.controller;

import daily_schedule.daily_schedule_be.domain.yesterday.dto.response.YesterdaySchedulesResponseDto;
import daily_schedule.daily_schedule_be.domain.yesterday.service.YesterdaySchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/yesterday")
public class YesterdaySchedulesController {
    private final YesterdaySchedulesService yesterdayService;

     // 어제 대시보드 통계 데이터 조회
     // 요청 예시: /api/yesterday?date=2023-11-21
    @GetMapping
    public ResponseEntity<YesterdaySchedulesResponseDto> getYesterdayStatistics(
            @RequestParam String date,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        // 날짜 파라미터가 없으면 시스템 상 어제 날짜를 기본값으로 사용
        if (date == null) {
            date = String.valueOf(LocalDate.now().minusDays(1));
        }

        YesterdaySchedulesResponseDto response = yesterdayService.getDailyStatistics(userId, date);
        return ResponseEntity.ok(response);
    }
}
