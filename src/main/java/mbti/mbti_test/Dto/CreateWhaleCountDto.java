package mbti.mbti_test.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mbti.mbti_test.domain.WhaleCount;

@Data
@Getter @Setter
public class CreateWhaleCountDto { // 필요해서 생성 0804

    private Long whaleId;
    private String whaleName;
    private int whaleCount;
    private double whaleShare;

    public CreateWhaleCountDto(WhaleCount whaleCount) {
        this.whaleId = whaleCount.getId();
        this.whaleName = whaleCount.getName();
        this.whaleCount = whaleCount.getCount();
        this.whaleShare = whaleCount.getShare();
    }
}
