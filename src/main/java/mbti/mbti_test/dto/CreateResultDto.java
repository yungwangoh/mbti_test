package mbti.mbti_test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import mbti.mbti_test.domain.Result;

import java.time.LocalDateTime;

//0803 hayoon
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateResultDto {

    private Long resultId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime testDate;

    private CreateMemberDto member;
    private CreateWhaleCountDto whale;

    public CreateResultDto(Result result) {
        this.resultId = result.getId();
        this.testDate = result.getTestTime();
        this.member = new CreateMemberDto(result.getMember());
        this.whale = new CreateWhaleCountDto(result.getWhaleCount());
    }
}
