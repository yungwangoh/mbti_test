package mbti.mbti_test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore // 양방향 매핑때 추가 해야함 안그러면 무한루프에 빠짐
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "whaleCount_id")
    private WhaleCount whaleCount;

    public Result(Member member, WhaleCount whaleCount) {
        this.testTime = LocalDateTime.now();
        this.member = member;
        this.whaleCount = whaleCount;
    }

    // 비즈니스 로직
    // 비즈니스 로직이 들어갈 경우에는 생성자에다 말고 메서드를 따로 선언한다.
    public static Result createResult(Member member, WhaleCount whaleCount) {
        Result result = new Result(member, whaleCount);
        result.getWhaleCount().whaleCountValue(); // 비즈니스 로직
        return result;
    }
}
