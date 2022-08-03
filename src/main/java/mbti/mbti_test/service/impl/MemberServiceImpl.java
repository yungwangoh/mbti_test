package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.Address;
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

    /**
     * 회원가입
     * @param member
     * @return
     */
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

    /**
     * 전체 회원 조회
     * @return
     */
    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //0803 hayoon Setter <수정요망>
    @Override
    @Transactional
    public void updateMember(Long id, String account, String pwd, Address address, String email) {
        Member member = memberRepository.findOne(id); //영속성 상태
        member.setAccount(account);
        member.setPwd(pwd);
        member.setAddress(address);
        member.setEmail(email);

        //회원일 경우-> 테스트 결과지 회원상태로 수정 후 공유가능?
        //result.setMemberStatus(MemberStatus.MEMBER);
    }

    /**
     * 회원검증
     */
    private void userValidation(Member member) {
        List<Member> findByName = memberRepository.findByName(member.getName());
        if(!findByName.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
