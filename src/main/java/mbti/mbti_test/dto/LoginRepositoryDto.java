package mbti.mbti_test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Getter
@AllArgsConstructor
public class LoginRepositoryDto {

    private String accessToken;
    private String refreshToken;
}
