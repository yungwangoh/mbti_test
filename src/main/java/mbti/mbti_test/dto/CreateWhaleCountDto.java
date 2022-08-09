package mbti.mbti_test.dto;

import lombok.*;
import mbti.mbti_test.domain.WhaleCount;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateWhaleCountDto { // 필요해서 생성 0804

    private Long whaleId;
    private String whaleName;
    private double whaleShare;

    public CreateWhaleCountDto(WhaleCount whaleCount) {
        this.whaleId = whaleCount.getId();
        this.whaleName = whaleCount.getName();
        this.whaleShare = whaleCount.getShare();
    }
}
