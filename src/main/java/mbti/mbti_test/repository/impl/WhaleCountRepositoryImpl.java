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

    @Override
    public void initWhaleMethod() {
        WhaleCount whaleCount1 = new WhaleCount("일각고래", 0);
        WhaleCount whaleCount2 = new WhaleCount("상괭이",0);
        WhaleCount whaleCount3 = new WhaleCount("고양이고래", 0);
        WhaleCount whaleCount4 = new WhaleCount("병코돌고래", 0);
        WhaleCount whaleCount5 = new WhaleCount("대왕고래", 0);
        WhaleCount whaleCount6 = new WhaleCount("북극고래", 0);
        WhaleCount whaleCount7 = new WhaleCount("남방큰돌고래", 0);
        WhaleCount whaleCount8 = new WhaleCount("벨루가", 0);
        WhaleCount whaleCount9 = new WhaleCount("민부리고래", 0);
        WhaleCount whaleCount10 = new WhaleCount("밍크고래", 0);
        WhaleCount whaleCount11 = new WhaleCount("범고래", 0);
        WhaleCount whaleCount12 = new WhaleCount("남방참고래", 0);
        WhaleCount whaleCount13 = new WhaleCount("귀신고래", 0);
        WhaleCount whaleCount14 = new WhaleCount("항유고래", 0);
        WhaleCount whaleCount15 = new WhaleCount("낫골고래", 0);
        WhaleCount whaleCount16 = new WhaleCount("혹등고래", 0);

        save(whaleCount1);
        save(whaleCount2);
        save(whaleCount3);
        save(whaleCount4);
        save(whaleCount5);
        save(whaleCount6);
        save(whaleCount7);
        save(whaleCount8);
        save(whaleCount9);
        save(whaleCount10);
        save(whaleCount11);
        save(whaleCount12);
        save(whaleCount13);
        save(whaleCount14);
        save(whaleCount15);
        save(whaleCount16);
    }
}
