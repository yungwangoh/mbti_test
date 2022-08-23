package mbti.mbti_test.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePwdDto {

    private String password;

    public ChangePwdDto(String password) {
        this.password = password;
    }
}
