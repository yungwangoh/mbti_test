package mbti.mbti_test.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class WhaleCount {

    @Id @GeneratedValue
    @Column(name = "whaleCount_id")
    private Long id;

    private String name;

    private int count;

    private double share; // 유저들의 고래 점유율

    public WhaleCount(String name, double share) {
        this.share = share;
        this.name = name;
        this.count = 0;
    }

    // 선택한 고래 카운트
    public void whaleCountValue() {
        this.count++;
    }

    public void whaleCountValueV2(WhaleCount whaleCount) {

        
    }
}
