package mbti.mbti_test.service.impl;

import mbti.mbti_test.domain.*;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.repository.ResultRepository;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import mbti.mbti_test.service.WhaleCountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static mbti.mbti_test.domain.MbtiList.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResultServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ResultService resultService;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    WhaleCountRepository whaleCountRepository;

    @Autowired
    WhaleCountService whaleCountService;

    @Autowired
    EntityManager em;

    @Test
    public void 결과() throws Exception {
        Address address = getAddress("인천시", "문화로", "123");
        Member member = createMember("윤광오", address, "fhjkadshf", MemberStatus.USER);
        memberService.join(member);
    }

    @Test
    public void 유저고래조회() throws Exception {
        Address address = getAddress("인천시", "문화로", "123");
        Member member = createMember("윤광오", address, "fhjkadshf", MemberStatus.USER);

    }

    @Test
    public void 유저가제일많이선택한고래() throws Exception {
        Address address = getAddress("인천시", "문화로", "123");
        Member member = createMember("윤광오", address, "swager253@daum.net", MemberStatus.USER);
        memberService.join(member);

        Address address1 = getAddress("용인시", "문화로", "123");
        Member member1 = createMember("하윤", address1, "swager253@daum.net", MemberStatus.USER);
        memberService.join(member1);

        Address address2 = getAddress("서울시", "문화로", "123");
        Member member2 = createMember("최용재", address2, "swager253@daum.net", MemberStatus.USER);
        memberService.join(member2);

        Address address3 = getAddress("의정부시", "문화로", "123");
        Member member3 = createMember("김경민", address3, "swager253@daum.net", MemberStatus.USER);
        memberService.join(member3);

        Result whaleTest = createWhaleTest(member, ENFJ);
        Result whaleTest1 = createWhaleTest(member1, ENFJ);
        Result whaleTest2 = createWhaleTest(member2, ENFJ);
        Result whaleTest3 = createWhaleTest(member3, ENTJ);

        System.out.println("whaleTest = " + whaleTest.getWhaleCount().getCount());

        List<WhaleCount> whaleNames = whaleCountRepository.findWhaleName(whaleTest.getWhaleCount().getName());

    }

    private Result createWhaleTest(Member member, MbtiList mbtiList) {
        WhaleCount whaleCount = new WhaleCount(mbtiList.whaleNameMethod(), 0);
        whaleCountRepository.save(whaleCount);
        Result result = Result.createResult(member, mbtiList, whaleCount);
        resultRepository.save(result);
        return result;
    }

    private Address getAddress(String city, String street, String zipcode) {
        return new Address(city, street, zipcode);
    }

    private Member createMember(String name, Address address, String email, MemberStatus memberStatus) {
        return new Member(name, address, email, memberStatus);
    }

}