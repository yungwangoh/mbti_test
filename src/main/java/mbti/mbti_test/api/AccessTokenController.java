package mbti.mbti_test.api;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.config.security.access.AccessService;
import mbti.mbti_test.config.security.jwt.JwtTokenProvider;
import mbti.mbti_test.dto.LoginRepositoryDto;
import mbti.mbti_test.redis.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.valueOf;

@RestController
@RequiredArgsConstructor
public class AccessTokenController { // access token 재발급 위한 컨트롤러

    private final AccessService accessService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @GetMapping("/api/reissue") // 엑세스 토큰 재발급
    public ResponseEntity<LoginRepositoryDto> reIssue(@RequestParam("account") String account,
                                                      @RequestHeader("refresh-Token") String refreshToken) {

        LoginRepositoryDto loginRepositoryDto = accessService.reIssueAccessToken(account, refreshToken);
        return new ResponseEntity<>(loginRepositoryDto, OK);
    }

    @GetMapping("/api/token/validation") // 토큰 유효성 검사
    public ResponseEntity<?> tokenValidation(@RequestParam("account") String account,
                                             @RequestHeader("access-Token") String accessToken) {

        if(jwtTokenProvider.checkAccessToken(accessToken)) { // 엑세스 토큰이 만료되었다면,
            if(jwtTokenProvider.checkRefreshToken(account, redisService.getValues(account))) { // 리프레시 토큰이 만료되었다면
                return new ResponseEntity<>(valueOf(1000)); // front-end login redirection.
            }                                                                     // custom http-state-code.
            else { // 리프레시 토큰이 존재함 -> 엑세스 토큰 재발급.
                this.reIssue(account, redisService.getValues(account));
            }
        }
        return new ResponseEntity<>(OK); // 재발급 안해도 됨.
    }
}
