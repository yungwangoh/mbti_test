package mbti.mbti_test.repository.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.Result;
import mbti.mbti_test.domain.ResultSearch;
import mbti.mbti_test.repository.ResultRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResultRepositoryImpl implements ResultRepository {

    private final EntityManager em;

    @Override
    public void save(Result result) {
        em.persist(result);
    }

    @Override
    public Result findOne(Long id) {
        return em.find(Result.class, id);
    }

    @Override
    public List<Result> findAll() {
        return em.createQuery("select r from Result r", Result.class)
                .getResultList();
    }

    @Override
    public List<Result> findMemberResult(Long memberId) { // 페이징 처리? -> 최적화...
        return em.createQuery("select r from Result r " +
                        " join fetch r.whaleCount" +
                        " join fetch r.member m where m.id = :memberId order by r.id desc", Result.class)
                .setFirstResult(0)
                .setMaxResults(5)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<Result> findWithMemberWhaleRepo() { // fetch join 최적화 메서드
        return em.createQuery("select r from Result r" +
                " join fetch r.member" +
                " join fetch r.whaleCount", Result.class)
                .getResultList();
    }
}
