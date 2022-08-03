package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.*;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.repository.ResultRepository;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static mbti.mbti_test.domain.MbtiList.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultServiceImpl implements ResultService {

    private final MemberRepository memberRepository;
    private final ResultRepository resultRepository;
    private final WhaleCountRepository whaleCountRepository;

    @Override
    @Transactional
    public Long ResultJoin(Result result) {
        resultRepository.save(result);
        return result.getId();
    }

    @Override
    public String ResultWhale(Long resultId) {
        Result findResult = resultRepository.findOne(resultId);
        return findResult.getMbtiList().whaleNameMethod();
    }

    @Override
    public double userWhaleShare(double Share) { // 유저들이 선택한 고래의 점유율
        return 0;
    }

    @Override
    public List<Result> findWhaleResults(ResultSearch resultSearch) {
        return resultRepository.findAllByWhale(resultSearch);
    }

    @Override
    public MbtiList mbtiChangeEnum(String mbti) {    // 문자열로 들어론 mbti를 enum type 으로 반환
                                                    // 없을 경우 null을 반환.
        if(mbti == "ISTJ")
            return ISTJ;
        else if (mbti == "ISFJ") {
            return ISFJ;
        } else if (mbti == "INFJ") {
            return INFJ;
        } else if (mbti == "INTJ") {
            return INTJ;
        } else if (mbti == "ISTP") {
            return ISTP;
        } else if (mbti == "ISFP") {
            return ISFP;
        } else if (mbti == "INFP") {
            return INFP;
        } else if (mbti == "INTP") {
            return INTP;
        } else if (mbti == "ESTP") {
            return ESTP;
        } else if (mbti == "ESFP") {
            return ESFP;
        } else if (mbti == "ENFP") {
            return ENFP;
        } else if (mbti == "ENTP") {
            return ENTP;
        } else if (mbti == "ESTJ") {
            return ESTJ;
        } else if (mbti == "ESFJ") {
            return ESFJ;
        } else if (mbti == "ENFJ") { // 여기 mbti가 2개 빠졌었는데 추가했음.
            return ENFJ;
        } else if (mbti == "ENTJ") {
            return ENTJ;
        } else
            return null;
    }
}
