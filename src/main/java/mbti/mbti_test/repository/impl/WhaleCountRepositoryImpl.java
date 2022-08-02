package mbti.mbti_test.repository.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
    public void initWhaleMethod() {
        List<WhaleCount> whaleCounts = new ArrayList<>() {
            {
                add(new WhaleCount("일각고래", 0));
                add(new WhaleCount("상괭이", 0));
                add(new WhaleCount("고양이고래", 0));
                add(new WhaleCount("병코돌고래", 0));
                add(new WhaleCount("대왕고래", 0));
                add(new WhaleCount("북극고래", 0));
                add(new WhaleCount("남방큰돌고래", 0));
                add(new WhaleCount("벨루가", 0));
                add(new WhaleCount("민부리고래", 0));
                add(new WhaleCount("밍크고래", 0));
                add(new WhaleCount("범고래", 0));
                add(new WhaleCount("남방참고래", 0));
                add(new WhaleCount("귀신고래", 0));
                add(new WhaleCount("항유고래", 0));
                add(new WhaleCount("낫골고래", 0));
                add(new WhaleCount("혹등고래", 0));
            }
        };

        int value = 100;
        for (WhaleCount whaleCount : whaleCounts) {
            for(int i = 10; i < value; i++)
                whaleCount.whaleCountValue();
            save(whaleCount);
            value--;
        }

        // Max_Value
    }

}
