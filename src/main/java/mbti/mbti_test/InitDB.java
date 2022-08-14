package mbti.mbti_test;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.*;
import mbti.mbti_test.repository.WhaleCountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

// API를 테스트하기위한 DB데이터 초기화
@Component
@RequiredArgsConstructor
@Transactional
public class InitDB {

    private final WhaleCountRepository whaleCountRepository;
    private final EntityManager em;

    @Bean
    public void init() {
        List<WhaleCount> whaleCounts = whaleCountRepository.initWhaleMethod();
        whaleCounts.forEach(whaleCount -> {
            em.persist(whaleCount);
        });
    }
}
