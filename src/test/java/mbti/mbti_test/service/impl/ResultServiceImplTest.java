package mbti.mbti_test.service.impl;

import mbti.mbti_test.domain.*;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResultServiceImplTest {

    @Autowired MemberService memberService;
    @Autowired WhaleCountRepository whaleCountRepository;
    @Autowired ResultService resultService;

    @Autowired EntityManager em;

    @Test
    @Rollback(value = false)
    public void 유저가고른고래의수() {
        whaleCountRepository.initWhaleMethod();
        createMembers();
    }

    private void createMembers() {
        Address address1 = getAddress("인천시", "문화로", "123");
        Address address2 = getAddress("서울시", "강남구", "일원1동");
        Address address3 = getAddress("성남시", "분당구", "정자2동");

        Member member1 = createMember("윤광오", "qkfks1234", "1234",
                address1, "swager253@naver.com", MemberStatus.USER);
        Member member2 = createMember("하윤", "gkdbssla97", "123456",
                address2, "gkdbssla97@naver.com", MemberStatus.NONUSER);
        Member member3 = createMember("김경민", "rlarudals123", "4321",
                address3, "kkm1112@naver.com", MemberStatus.USER);

        WhaleCount whaleName = whaleCountRepository.findWhaleName(MbtiList.ENFJ.whaleNameMethod());
        Result result = Result.createResult(member1, MbtiList.ENFJ, whaleName);

        WhaleCount whaleName1 = whaleCountRepository.findWhaleName(MbtiList.ENFJ.whaleNameMethod());
        Result result1 = Result.createResult(member2, MbtiList.ENFJ, whaleName1);

        WhaleCount whaleName3 = whaleCountRepository.findWhaleName(MbtiList.ENFP.whaleNameMethod());
        Result result3 = Result.createResult(member2, MbtiList.ENFP, whaleName3);

        WhaleCount one = whaleCountRepository.findOne(whaleName1.getId());
        one.getCount();
        resultService.ResultJoin(result);
        resultService.ResultJoin(result1);
        resultService.ResultJoin(result3);
    }

    private Address getAddress(String city, String street, String zipcode) {
        return new Address(city, street, zipcode);
    }

    private Member createMember(String name, String account, String pwd, Address address, String email, MemberStatus memberStatus) {
        Member member = new Member(name, account, pwd, address, email, memberStatus);
        memberService.join(member);
        return member;
    }
}