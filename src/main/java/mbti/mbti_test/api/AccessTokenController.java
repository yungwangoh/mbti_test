package mbti.mbti_test.api;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.config.security.access.AccessService;
import mbti.mbti_test.config.security.jwt.JwtTokenProvider;
import mbti.mbti_test.dto.LoginRepositoryDto;
import mbti.mbti_test.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class AccessTokenController { // access token 재발급 위한 컨트롤러

    private final AccessService accessService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @GetMapping("/api/reissue") // 엑세스 토큰 만료, 재발급
    public ResponseEntity<LoginRepositoryDto> reIssue(@RequestParam("account") String account,
                                                  @RequestHeader("refresh-Token") String refreshToken) {

        LoginRepositoryDto loginRepositoryDto = accessService.reIssueAccessToken(account, refreshToken);
        return new ResponseEntity<>(loginRepositoryDto, OK);
    }

    // 0829 엑세스 토큰 만료되면 재발급. -> 테스팅을 해보지 못했음 -> 0830 api 통신 테스트 완료 -> 프론트와 붙여서 테스팅 시작해야함.
    // 403 : forbidden -> 리프레시 토큰이 없어서 로그인창으로 리디렉션.
    @GetMapping("/api/access/expired")
    public ResponseEntity<LoginRepositoryDto> reAccessExpired(@RequestParam("account") String account,
                                                                  @RequestHeader("access-Token") String accessToken) throws ResponseStatusException {

        boolean expiredToken = jwtTokenProvider.checkAccessToken(accessToken); // 엑세스 토큰 만료일자 확인.
        if(expiredToken) { // 만료된 토큰이라면, (Access)
            try {
                LoginRepositoryDto loginRepositoryDto =
                        accessService.reIssueAccessToken(account, redisService.getValues(account)); // 엑세스 토큰 재발급.

                return new ResponseEntity<>(loginRepositoryDto, OK);
            } catch (Exception e) { // Non-Access, Non-Refresh
                // 403 : forbidden -> reason : Non-Access, Non-Refresh.
                throw new ResponseStatusException(FORBIDDEN, "No Refresh-Token Login-redirection");
            }
        }
        else { // 만료되지 않은 토큰이라면, 엑세스 토큰 반환.
            try {
                return new ResponseEntity<>(new LoginRepositoryDto(accessToken, redisService.getValues(account)), OK);
            } catch (Exception e) { // Non-refresh error
                throw new ResponseStatusException(FORBIDDEN, "No Refresh-Token Login-redirection");
            }
        }
    }
}
