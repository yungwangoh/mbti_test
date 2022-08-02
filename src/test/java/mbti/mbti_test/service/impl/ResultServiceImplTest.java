package mbti.mbti_test.service.impl;

import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.repository.WhaleCountRepository;
import mbti.mbti_test.service.ResultService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResultServiceImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WhaleCountRepository whaleCountRepository;

    @Autowired
    ResultService resultService;

    @Autowired
    EntityManager em;

    @Test
    public void 유저가고른고래의수() {

        createMembers();
    }

    private void createMembers() {
        Address address = getAddress("인천시", "문화로", "123");
        Member member = createMember("윤광오", "qkfks1234", "1234",
                address, "swager253@naver.com", MemberStatus.USER);
    }

    private Address getAddress(String city, String street, String zipcode) {
        return new Address(city, street, zipcode);
    }

    private Member createMember(String name, String account, String pwd, Address address, String email, MemberStatus memberStatus) {
        return new Member(name, account, pwd, address, email, memberStatus);
    }
}