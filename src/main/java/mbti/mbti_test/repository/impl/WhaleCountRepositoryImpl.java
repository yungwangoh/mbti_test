package mbti.mbti_test.repository.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WhaleCountRepositoryImpl implements WhaleCountRepository {

    private final EntityManager em;

    @Override
    public void save(WhaleCount whaleCount) {
        em.persist(whaleCount);
    }

    @Override
    public WhaleCount findOne(Long id) {
        return em.find(WhaleCount.class, id);
    }


    @Override
    public List<WhaleCount> findMaxOne() {
        return em.createQuery("select MAX (w.count) from WhaleCount w", WhaleCount.class)
                .getResultList();
    }

    @Override
    public List<WhaleCount> findMinOne() {
        return em.createQuery("select MIN (w.count) from WhaleCount w", WhaleCount.class)
                .getResultList();
    }

    @Override
    public List<WhaleCount> findWhaleName(String name) {
        return em.createQuery("select w from WhaleCount w where w.name = :name", WhaleCount.class)
                .setParameter("name", name)
                .getResultList();
    }

    @Override
    public List<WhaleCount> findAll() {
        return em.createQuery("select w from WhaleCount w", WhaleCount.class)
                .getResultList();
    }
}
