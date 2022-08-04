package mbti.mbti_test.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.ResultSearch;

@Data
@Getter @Setter
public class ResultSearchDto {

    private String memberName;
    private MbtiList mbtiList;

    public ResultSearchDto(ResultSearch resultSearch) {
        this.memberName = resultSearch.getMemberName();
        this.mbtiList = resultSearch.getMbtiList();
    }
}
