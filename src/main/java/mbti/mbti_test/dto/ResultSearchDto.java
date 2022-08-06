package mbti.mbti_test.dto;

import lombok.*;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.ResultSearch;

@Data
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultSearchDto {

    private String memberName;
    private MbtiList mbtiList;

    public ResultSearchDto(ResultSearch resultSearch) {
        this.memberName = resultSearch.getMemberName();
        this.mbtiList = resultSearch.getMbtiList();
    }
}
