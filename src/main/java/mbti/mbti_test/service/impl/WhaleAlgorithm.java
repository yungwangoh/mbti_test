package mbti.mbti_test.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.WhaleCountService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 여기 추가
public class WhaleAlgorithm {

    private WhaleCountService whaleCountService;

    private int iCount;
    private int eCount;

    private int sCount;
    private int nCount;

    private int tCount;
    private int fCount;

    private int pCount;
    private int jCount;

    public WhaleAlgorithm(int iCount, int eCount, int sCount, int nCount, int tCount, int fCount, int pCount, int jCount) {
        this.iCount = iCount;
        this.eCount = eCount;
        this.sCount = sCount;
        this.nCount = nCount;
        this.tCount = tCount;
        this.fCount = fCount;
        this.pCount = pCount;
        this.jCount = jCount;
    }

    public String ieSelect(int iCount, int eCount) {

        if((iCount >= 0 && iCount <= 3) && (eCount >= 0 && eCount <= 3)) {
            if (iCount > eCount) return "I";
            else return "E";
        }
        else throw new IllegalStateException("Invalid Error!!");
    }

    public String snSelect(int sCount, int nCount) {

        if((sCount >= 0 && sCount <= 3) && (nCount >= 0 && nCount <= 3)) {
            if (sCount > nCount) return "S";
            else return "N";
        }
        else throw new IllegalStateException("Invalid Error!!");
    }

    public String tfSelect(int tCount, int fCount) {

        if((tCount >= 0 && tCount <= 3) && (fCount >= 0 && fCount <= 3)) {
            if (tCount > fCount) return "T";
            else return "F";
        } else throw new IllegalStateException("Invalid Error!!");
    }

    public String pjSelect(int pCount, int jCount) {

        if((pCount >= 0 && pCount <= 3) && (jCount >= 0 && jCount <= 3)) {
            if (pCount > jCount) return "P";
            else return "J";
        } else throw new IllegalStateException("Invalid Error");
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
    //짝꿍조합(ISTP - ESFP),
}

