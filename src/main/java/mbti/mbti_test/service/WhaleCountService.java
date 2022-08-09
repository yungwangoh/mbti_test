package mbti.mbti_test.service;

import mbti.mbti_test.Dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.WhaleCount;

import java.util.List;

public interface WhaleCountService {

    Long whaleJoin(WhaleCount whaleCount);

    WhaleCount findOne(Long whaleId);

    WhaleCount findWhaleNameMbti(String WhaleName);

    List<WhaleCount> whaleSortDescend();

    List<WhaleCount> maxWhaleNameShare();

    List<WhaleCount> minWhaleNameShare();

    List<WhaleCount> whaleCompatibility(CreateWhaleCountDto createWhaleCountDto);

    List<WhaleCount> findAll();
}
