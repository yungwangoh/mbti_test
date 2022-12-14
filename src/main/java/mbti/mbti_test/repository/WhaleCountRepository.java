package mbti.mbti_test.repository;

import mbti.mbti_test.domain.WhaleCount;

import java.util.List;

public interface WhaleCountRepository {

    void save(WhaleCount whaleCount);

    WhaleCount findOne(Long id);

    List<WhaleCount> findMaxOne();

    List<WhaleCount> findMinOne();

    WhaleCount findWhaleName(String name);

    List<WhaleCount> findAll();

    List<WhaleCount> initWhaleMethod();

    List<WhaleCount> sharePoint();
}
