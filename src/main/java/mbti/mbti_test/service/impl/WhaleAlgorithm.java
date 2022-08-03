package mbti.mbti_test.service.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 여기 추가W
public class WhaleAlgorithm {

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

        if(iCount > eCount) return "I";
        else return "E";
    }

    public String snSelect(int sCount, int nCount) {
        if(sCount > nCount) return "S";
        else return "N";
    }

    public String tfSelect(int tCount, int fCount) {
        if(tCount > fCount) return "T";
        else return "F";
    }

    public String pjSelect(int pCount, int jCount) {
        if(pCount > jCount) return "P";
        else return "J";
    }

    public String mbtiCombination(String ie, String sn, String tf, String pj) {
        return ie + sn + tf + pj;
    }

    //점유율 계산, 짝꿍조합(ISTP - ESFP),
}
