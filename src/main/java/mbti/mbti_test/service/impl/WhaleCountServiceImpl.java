package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.WhaleCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhaleCountServiceImpl implements WhaleCountService {

    private final WhaleCountRepository whaleCountRepository;

    @Override
    @Transactional
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

    @Override
    public List<WhaleCount> whaleCompatibility(CreateWhaleCountDto createWhaleCountDto) {

        if(createWhaleCountDto.getWhaleName().equals("일각고래")) {
            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("혹등고래")); // 최고의 궁합
                    add(whaleCountRepository.findWhaleName("남방참고래")); // 최악의 궁합
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("상괭이")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("남방참고래"));
                    add(whaleCountRepository.findWhaleName("혹등고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("고양이고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("향유고래"));
                    add(whaleCountRepository.findWhaleName("밍크고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("병코돌고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("밍크고래"));
                    add(whaleCountRepository.findWhaleName("향유고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("대왕고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("낫돌고래"));
                    add(whaleCountRepository.findWhaleName("범고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("북극고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("범고래"));
                    add(whaleCountRepository.findWhaleName("낫돌고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("남방큰돌고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("귀신고래"));
                    add(whaleCountRepository.findWhaleName("민부리고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("벨루가")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("민부리고래"));
                    add(whaleCountRepository.findWhaleName("귀신고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("민부리고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("벨루가"));
                    add(whaleCountRepository.findWhaleName("남방큰돌고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("밍크고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("병코돌고래"));
                    add(whaleCountRepository.findWhaleName("고양이고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("범고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("북극고래"));
                    add(whaleCountRepository.findWhaleName("대왕고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("남방참고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("상괭이"));
                    add(whaleCountRepository.findWhaleName("일각고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("귀신고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("남방큰돌고래"));
                    add(whaleCountRepository.findWhaleName("벨루가"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("향유고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("고양이고래"));
                    add(whaleCountRepository.findWhaleName("병코돌고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("낫돌고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("대왕고래"));
                    add(whaleCountRepository.findWhaleName("북극고래"));
                }
            };

        } else if (createWhaleCountDto.getWhaleName().equals("혹등고래")) {

            return new ArrayList<>() {
                {
                    add(whaleCountRepository.findWhaleName("일각고래"));
                    add(whaleCountRepository.findWhaleName("상괭이"));
                }
            };

        } else throw new IllegalStateException("해당하는 고래가 없습니다.");
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
