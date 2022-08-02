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

}
