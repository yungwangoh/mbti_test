package mbti.mbti_test.api;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.config.security.access.AccessService;
import mbti.mbti_test.dto.LoginRepositoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class AccessTokenController { // access token 재발급 위한 컨트롤러

    private final AccessService accessService;

    //0823 Hayoon
    //AccessToken 재발급
    @GetMapping("/api/v1/re-issue")
    public ResponseEntity<LoginRepositoryDto> reIssue(@RequestParam("refreshToken") String refreshToken,
                                                      @RequestParam("account") String account) {
        LoginRepositoryDto loginRepositoryDto = accessService.reIssueAccessToken(account, refreshToken);
        return new ResponseEntity<>(loginRepositoryDto, OK);
    }

}
