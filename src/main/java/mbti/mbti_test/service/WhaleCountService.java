package mbti.mbti_test.service;

import mbti.mbti_test.domain.WhaleCount;

import java.util.List;

public interface WhaleCountService {

    Long whaleJoin(WhaleCount whaleCount);

    WhaleCount findOne(Long whaleId);

    List<WhaleCount> findAll();
}
