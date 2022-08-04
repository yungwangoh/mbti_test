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
    public List<Result> findWithMemberWhaleRepo() { // fetch join 최적화 메서드
        return em.createQuery("select r from Result r" +
                " join fetch r.member m" +
                " join fetch r.whaleCount w", Result.class)
                .getResultList();
    }

    @Override
    public List<Result> findAllByWhale(ResultSearch resultSearch) {
        //language = JPAQL
        String jpql = "select r From Result r join r.mbtiList m";
        boolean isFirstCondition = true;

        //결과 고래 종류별 검색
        if (resultSearch.getMbtiList() != null) {
            if (isFirstCondition) {
                jpql += "where";
                isFirstCondition = false;
            } else {
                jpql += "and";
            }
            jpql += "r.type =: type";
        }
        /**
         * (현재 미구현 파트)
         //회원 이름별 검색
         if (StringUtils.hasText(resultSearch.getMemberName())) {
         if (isFirstCondition) {
         jpql += "where";
         isFirstCondition = false;
         } else {
         jpql += "and";
         }
         jpql += "w.type like =: type";
         }**/

        TypedQuery<Result> query = em.createQuery(jpql, Result.class)
                .setMaxResults(10);
        if (resultSearch.getMbtiList() != null) {
            query = query.setParameter("type", resultSearch.getMbtiList().whaleNameMethod());
        }
        /**
         if (StringUtils.hasText(resultSearch.getMemberName())) {
         query = query.setParameter("name", resultSearch.getMemberName());
         }**/
        return query.getResultList();
    }

}
