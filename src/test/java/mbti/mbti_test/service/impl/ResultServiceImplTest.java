package mbti.mbti_test.service.impl;

import mbti.mbti_test.domain.*;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import mbti.mbti_test.service.WhaleCountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional(readOnly = true)
class ResultServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired WhaleCountRepository whaleCountRepository;

    @Autowired
    WhaleCountService whaleCountService;
    @Autowired ResultService resultService;

    @Autowired EntityManager em;

    @Test
    public void 문자열_MBTI를_ENUM으로_변환() { // 잘 작동하는것을 확인
        MbtiList mbtiList = resultService.mbtiChangeEnum("ISTP");
        assertEquals("귀신고래", mbtiList.whaleNameMethod());
    }

    @Test
    public void 고래정렬() {
        List<WhaleCount> whaleCounts = whaleCountService.whaleSortDescend();
        whaleCounts.forEach(e -> {
            System.out.println("e.getName() = " + e.getName() + " e.getCount() = " + e.getCount());
        });
    }
}