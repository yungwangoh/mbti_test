package mbti.mbti_test.repository.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.impl.WhaleAlgorithm;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
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
        return em.createQuery("select w from WhaleCount w where " +
                        " w.count = (select max(w.count) from WhaleCount w)", WhaleCount.class)
                .getResultList();
    }

    @Override
    public List<WhaleCount> findMinOne() {
        return em.createQuery("select w from WhaleCount w where " +
                        " w.count = (select min(w.count) from WhaleCount w)", WhaleCount.class)
                .getResultList();
    }

    @Override
    public WhaleCount findWhaleName(String name) {
        return em.createQuery("select w from WhaleCount w where w.name = :name", WhaleCount.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public List<WhaleCount> findAll() {
        return em.createQuery("select w from WhaleCount w", WhaleCount.class)
                .getResultList();
    }

    @Override
    public List<WhaleCount> sharePoint() {
        return null;
    }

    @Override
    public List<WhaleCount> initWhaleMethod() {
        List<WhaleCount> whaleCounts = Arrays.asList(

                new WhaleCount("일각고래", 0),
                new WhaleCount("상괭이", 0),
                new WhaleCount("고양이고래", 0),
                new WhaleCount("병코돌고래", 0),
                new WhaleCount("대왕고래", 0),
                new WhaleCount("북극고래", 0),
                new WhaleCount("남방큰돌고래", 0),
                new WhaleCount("벨루가", 0),
                new WhaleCount("민부리고래", 0),
                new WhaleCount("밍크고래", 0),
                new WhaleCount("범고래", 0),
                new WhaleCount("남방참고래", 0),
                new WhaleCount("귀신고래", 0),
                new WhaleCount("향유고래", 0),
                new WhaleCount("낫돌고래", 0),
                new WhaleCount("혹등고래", 0)
        );

        return whaleCounts;
    }
}



