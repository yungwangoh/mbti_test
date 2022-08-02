package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.WhaleCountService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<WhaleCount> findAll() {
        return whaleCountRepository.findAll();
    }
}
