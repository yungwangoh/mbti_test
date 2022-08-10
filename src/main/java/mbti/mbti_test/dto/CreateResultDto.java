package mbti.mbti_test.dto;

import lombok.*;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.Result;

import java.time.LocalDateTime;

//0803 hayoon
@Data
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateResultDto {

    private Long resultId;
    private LocalDateTime resultDate;
    private CreateMemberDto member;

    private CreateWhaleCountDto whale;

    public CreateResultDto(Result result) {
        this.resultId = result.getId();
        this.resultDate = result.getTestTime();
        this.member = new CreateMemberDto(result.getMember());
        this.whale = new CreateWhaleCountDto(result.getWhaleCount());
    }
}
