package mbti.mbti_test.service;

import mbti.mbti_test.domain.*;

import java.util.List;

public interface ResultService {

    Long ResultJoin(Result result);

    String ResultWhale(Long resultId);

    double userWhaleShare(double Share);

    List<Result> findWhaleResults(ResultSearch resultSearch);

    MbtiList mbtiChangeEnum(String mbti); // 문자열로 들어론 mbti를 enum type으로 반환

    List<Result> findMemberResultService(Long memberId);

    List<Result> resultAll();

    List<Result> findWithMemberWhale(); // fetch join 활용 최적화 메서드
}
