package mbti.mbti_test.Dto;

import lombok.*;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.ResultSearch;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultSearchDto {

    private String memberName;
    private MbtiList mbtiList;

    public ResultSearchDto(ResultSearch resultSearch) {
        this.memberName = resultSearch.getMemberName();
        this.mbtiList = resultSearch.getMbtiList();
    }
}
