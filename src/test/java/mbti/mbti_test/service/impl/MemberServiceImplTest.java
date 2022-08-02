package mbti.mbti_test.service.impl;

import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 회원가입() throws Exception {
        Address address = new Address("인천시", "문화로", "3214");
        Member member = new Member("kim", address, "sawget@afdsa.com", MemberStatus.USER);

        Long memberId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(memberId));
    }
}