package mbti.mbti_test.service.impl;

import lombok.*;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.WhaleCountService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.List;

@Data
@Service
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 여기 추가
public class WhaleAlgorithm {

    @NotNull
    private int iCount;

    @NotNull
    private int eCount;

    @NotNull
    private int sCount;

    @NotNull
    private int nCount;

    @NotNull
    private int tCount;

    @NotNull
    private int fCount;

    @NotNull
    private int pCount;

    @NotNull
    private int jCount;

    private List<WhaleCount> whaleCounts;

    public WhaleAlgorithm(List<WhaleCount> whaleCounts) {
        this.whaleCounts = whaleCounts;
    }

    public WhaleAlgorithm(WhaleAlgorithm whaleAlgorithm) {
        this.iCount = whaleAlgorithm.getICount();
        this.eCount = whaleAlgorithm.getECount();
        this.sCount = whaleAlgorithm.getSCount();
        this.nCount = whaleAlgorithm.getNCount();
        this.tCount = whaleAlgorithm.getTCount();
        this.fCount = whaleAlgorithm.getFCount();
        this.pCount = whaleAlgorithm.getPCount();
        this.jCount = whaleAlgorithm.getJCount();
    }

    public String ieSelect() {

        if((this.iCount >= 0 && this.iCount <= 3) && (this.eCount >= 0 && this.eCount <= 3)) {
            if (this.iCount > this.eCount) return "I";
            else if(this.iCount < this.eCount) return "E";
            else throw new IllegalStateException("Invalid Error");
        }
        else throw new IllegalStateException("Invalid Error!!");
    }

    public String snSelect() {

        if((this.sCount >= 0 && this.sCount <= 3) && (this.nCount >= 0 && this.nCount <= 3)) {
            if (this.sCount > this.nCount) return "S";
            else if(this.sCount < this.nCount) return "N";
            else throw new IllegalStateException("Invalid Error");
        }
        else throw new IllegalStateException("Invalid Error!!");
    }

    public String tfSelect() {

        if((this.tCount >= 0 && this.tCount <= 3) && (this.fCount >= 0 && this.fCount <= 3)) {
            if (this.tCount > this.fCount) return "T";
            else if (this.tCount < this.fCount) return "F";
            else throw new IllegalStateException("Invalid Error");
        }
        else throw new IllegalStateException("Invalid Error!!");
    }

    public String pjSelect() {

        if((this.pCount >= 0 && this.pCount <= 3) && (this.jCount >= 0 && this.jCount <= 3)) {
            if (this.pCount > this.jCount) return "P";
            else if (this.pCount < this.jCount) return "J";
            else throw new IllegalStateException("Invalid Error");
        }
        else throw new IllegalStateException("Invalid Error");
    }

    public String mbtiCombination(String ie, String sn, String tf, String pj) {
        return ie + sn + tf + pj;
    }

    //0804 Hayoon
    //점유율 계산
    //모든 고래의 COUNT가 0일경우 INFINITY 발생
    public void AllSharePoints(List<WhaleCount> whaleCountList) {
        int sum = 0;
        for (WhaleCount count : whaleCountList) {
            sum += count.getCount();
        }
        for (WhaleCount whaleCount : whaleCountList) {
            int point = whaleCount.getCount();
            //double share = Math.round((point * 100.0) / sum);
            double share = (point * 100.0) / sum;

            DecimalFormat df = new DecimalFormat("0.00");
            //DecimalFormat df = new DecimalFormat("#.##");
            // 0.00 과 #.## 모두 가능하나 둘의 차이점은 만약에 1.1일 경우
            // 0.00 은 나머지 0의 자리를 절삭해 1.1만 출력하지만
            //#.##은 0을 절삭하지 않아
            // 1.10을 출력한다.
            String result = df.format(share);
            //역시 리턴값이 String이기 때문에 String 변수에 담아주면 된다.
            //Double.parseDouble(Double)로 String->Double 자료형변환
            whaleCount.setSharePoint(Double.parseDouble(result));
            //whaleCountService.whaleJoin(whaleCount);
        }
    }
}

