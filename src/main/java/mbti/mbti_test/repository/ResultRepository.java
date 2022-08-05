package mbti.mbti_test.repository;

import mbti.mbti_test.domain.Result;
import mbti.mbti_test.domain.ResultSearch;

import java.util.List;

public interface ResultRepository {

    void save(Result result);
    Result findOne(Long id);
    List<Result> findAll();

    List<Result> findMemberResult(Long memberId);

    List<Result> findAllByWhale(ResultSearch resultSearch);

    List<Result> findWithMemberWhaleRepo(); // fetch join 최적화 메서드
}
