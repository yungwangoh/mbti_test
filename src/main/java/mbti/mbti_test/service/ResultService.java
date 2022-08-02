package mbti.mbti_test.service;

import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.Result;
import mbti.mbti_test.domain.WhaleCount;

public interface ResultService {

    Long ResultJoin(Result result);

    String ResultWhale(Long resultId);

    double userWhaleShare(double Share);
}
