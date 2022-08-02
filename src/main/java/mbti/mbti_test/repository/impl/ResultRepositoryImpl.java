package mbti.mbti_test.repository.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.Result;
import mbti.mbti_test.repository.ResultRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}
