package mbti.mbti_test.service;

import mbti.mbti_test.domain.*;

import java.util.List;

public interface ResultService {

    Long ResultJoin(Result result);

    String ResultWhale(Long resultId);

    double userWhaleShare(double Share);

    List<Result> findWhaleResults(ResultSearch resultSearch);
}
