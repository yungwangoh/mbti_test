package mbti.mbti_test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRepositoryDto {

    private String accessToken;
    private String refreshToken;
}
