package mbti.mbti_test.service.impl;

import mbti.mbti_test.api.WhaleCountApiController;
import mbti.mbti_test.dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.WhaleCountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@SpringBootTest
@Transactional(readOnly = true)
class WhaleCountServiceImplTest {

    @Autowired
    WhaleCountService whaleCountService;

    @Autowired
    WhaleCountRepository whaleCountRepository;

    @Autowired
    WhaleCountApiController whaleCountApiController;

    @Autowired
    EntityManager em;

    @Test
    public void 고래등록() {
        MbtiList mbtiList = MbtiList.ENFJ;
        String whaleName = mbtiList.whaleNameMethod();

        MbtiList mbtiList1 = MbtiList.ENFP;
        String whaleName1 = mbtiList1.whaleNameMethod();

        WhaleCount whaleCount = new WhaleCount(whaleName,0);
        WhaleCount whaleCount1 = new WhaleCount(whaleName1,0);

        Long whaleId = whaleCountService.whaleJoin(whaleCount);
        Long whaleId1 = whaleCountService.whaleJoin(whaleCount1);

        int count = whaleCount.getCount();
        System.out.println("ID = " + whaleId + " count1 = " + count);

        whaleCount.whaleCountValue();

        int count2 = whaleCount.getCount();
        System.out.println("ID = " + whaleId + " count2 = " + count2);

        int count3 = whaleCount1.getCount();
        System.out.println("ID = " + whaleId1 + " count3 = " + count3);

        whaleCount1.whaleCountValue();

        int count4 = whaleCount1.getCount();
        System.out.println("ID = " + whaleId1 + " count4 = " + count4);
    }

    @Test
    public void 고래궁합() {
        WhaleCount 일각고래 = whaleCountRepository.findWhaleName("고양이고래");
        CreateWhaleCountDto createWhaleCountDto = new CreateWhaleCountDto(일각고래);

        List<WhaleCount> whaleCounts = whaleCountService.whaleCompatibility(createWhaleCountDto);

        whaleCounts.forEach(e -> {
            System.out.println("e.getName() = " + e.getName());
        });
    }

    @Test
    public void 고래찾기() {
        WhaleCount 고양이고래 = whaleCountRepository.findWhaleName("병코돌고래");
        System.out.println("고양이고래 = " + 고양이고래.getName());
    }
}