package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long join(Member member) {
        userValidation(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    private void userValidation(Member member) {
        List<Member> findByName = memberRepository.findByName(member.getName());
        if(!findByName.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}