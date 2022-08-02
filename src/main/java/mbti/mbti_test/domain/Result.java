package mbti.mbti_test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result {

    @Id @GeneratedValue
    @Column(name = "result_id")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime testTime; // 테스트 한 시각

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MbtiList mbtiList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "whaleCount_id")
    private WhaleCount whaleCount;

    public Result(Member member, MbtiList mbtiList, WhaleCount whaleCount) {
        this.testTime = LocalDateTime.now();
        this.member = member;
        this.mbtiList = mbtiList;
        this.whaleCount = whaleCount;
    }

    // 비즈니스 로직
    // 비즈니스 로직이 들어갈 경우에는 생성자에다 말고 메서드를 따로 선언한다.
    public static Result createResult(Member member, MbtiList mbtiList, WhaleCount whaleCount) {
        Result result = new Result(member, mbtiList, whaleCount);
        result.getWhaleCount().whaleCountValue(); // 비즈니스 로직
        return result;
    }
}
