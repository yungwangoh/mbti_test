package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.WhaleCountService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhaleCountServiceImpl implements WhaleCountService {

    private final WhaleCountRepository whaleCountRepository;

    @Override
    public Long whaleJoin(WhaleCount whaleCount) {
        whaleCountRepository.save(whaleCount);
        return whaleCount.getId();
    }

    @Override
    public WhaleCount findOne(Long whaleId) {
        return whaleCountRepository.findOne(whaleId);
    }

    @Override
    public WhaleCount findWhaleNameMbti(String WhaleName) { // 0805 ygo 서비스 추가
        return whaleCountRepository.findWhaleName(WhaleName);
    }

    @Override
    public List<WhaleCount> maxWhaleNameShare() {
        return whaleCountRepository.findMaxOne();
    }

    @Override
    public List<WhaleCount> minWhaleNameShare() {
        return whaleCountRepository.findMinOne();
    }

    @Override
    public List<WhaleCount> findAll() {
        return whaleCountRepository.findAll();
    }

    @Override
    public List<WhaleCount> whaleSortDescend() {
        List<WhaleCount> whaleCountSort = whaleCountRepository.findAll();

        Collections.sort(whaleCountSort, new WhaleComparator());

        return whaleCountSort;
    }

    public static class WhaleComparator implements Comparator<WhaleCount> {

        @Override
        public int compare(WhaleCount o1, WhaleCount o2) {
            if(o1.getCount() > o2.getCount())
                return -1;
            else if(o1.getCount() == o2.getCount())
                return 0;
            else
                return 1;
        }
    }
}
