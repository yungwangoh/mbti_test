package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.Result;
import mbti.mbti_test.repository.ResultRepository;
import mbti.mbti_test.service.ResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static mbti.mbti_test.domain.MbtiList.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    @Override
    @Transactional
    public Long ResultJoin(Result result) {
        resultRepository.save(result);
        return result.getId();
    }

    @Override
    public String ResultWhale(Long resultId) {
        Result findResult = resultRepository.findOne(resultId);
        return findResult.getWhaleCount().getName();
    }

    @Override
    public double userWhaleShare(double Share) { // 유저들이 선택한 고래의 점유율
        return 0;
    }

    @Override // 0804 생성
    public List<Result> resultAll() {
        return resultRepository.findAll();
    }

    @Override
    public List<Result> findWithMemberWhale() { // fetch join 활용 최적화 메서드
        return resultRepository.findWithMemberWhaleRepo();
    }

    @Override
    public List<Result> findMemberResultService(Long memberId) {
        return resultRepository.findMemberResult(memberId);
    }

    @Override
    public MbtiList mbtiChangeEnum(String mbti) {    // 문자열로 들어론 mbti를 enum type 으로 반환
                                                    // 없을 경우 null을 반환.

        // 0804 Hayoon
        // 주소값 비교(==)와 값 비교(equals) 중 Int, boolean을 제외하고는 모두 equals()로 판별
        if(mbti.equals("ISTJ"))
            return ISTJ;
        else if (mbti.equals("ISFJ")) {
            return ISFJ;
        } else if (mbti.equals("INFJ")) {
            return INFJ;
        } else if (mbti.equals("INTJ")) {
            return INTJ;
        } else if (mbti.equals("ISTP")) {
            return ISTP;
        } else if (mbti.equals("ISFP")) {
            return ISFP;
        } else if (mbti.equals("INFP")) {
            return INFP;
        } else if (mbti.equals("INTP")) {
            return INTP;
        } else if (mbti.equals("ESTP")) {
            return ESTP;
        } else if (mbti.equals("ESFP")) {
            return ESFP;
        } else if (mbti.equals("ENFP")) {
            return ENFP;
        } else if (mbti.equals("ENTP")) {
            return ENTP;
        } else if (mbti.equals("ESTJ")) {
            return ESTJ;
        } else if (mbti.equals("ESFJ")) {
            return ESFJ;
        } else if (mbti.equals("ENFJ")) { // 여기 mbti가 2개 빠졌었는데 추가했음.
            return ENFJ;
        } else if (mbti.equals("ENTJ")) {
            return ENTJ;
        } else
            throw new RuntimeException("찾는 고래가 없습니다.");
    }
}
