package mbti.mbti_test.service.impl;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;
import mbti.mbti_test.dto.CreateMemberDto;
import mbti.mbti_test.dto.UpdateMemberDto;
import mbti.mbti_test.exception.MemberAlreadyExistException;
import mbti.mbti_test.repository.MemberRepository;
import mbti.mbti_test.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
//0813 Hayoon
//UserService -> MemberSerivceImpl로 새로 교체
public class MemberServiceImpl implements MemberService {

    private final MemberLoginRepository memberLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param
     * @return
     */
    @Transactional
    public Long join(CreateMemberDto createMemberDto) throws MemberAlreadyExistException {
        userValidation(createMemberDto);
        Long memberId = getRole_user(createMemberDto);
        return memberId;
    }

    public Long getRole_user(CreateMemberDto createMemberDto) {

        return memberLoginRepository.save(Member.builder()
                        .account(createMemberDto.getAccount())
                        .pwd(passwordEncoder.encode(createMemberDto.getPwd()))
                        .address(createMemberDto.getAddress())
                        .email(createMemberDto.getEmail())
                        .name(createMemberDto.getName())
                        .createDateTime(LocalDateTime.now())
                        .updateDateTime(LocalDateTime.now())
                        .memberStatus(MemberStatus.USER)
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build())
                .getId();
    }

    @Override
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Override
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional
    public void updateMember(Member findMember, UpdateMemberDto updateMemberDto) {
        Member member  = memberRepository.findOne(findMember.getId());
        member.updateMember(updateMemberDto.getAccount(), passwordEncoder.encode(updateMemberDto.getPwd()),
                updateMemberDto.getAddress(), updateMemberDto.getEmail());
    }

    /**
     * 회원검증
     */
    @Override
    public void userValidation(CreateMemberDto createMemberDto) throws MemberAlreadyExistException {
        Optional<Member> findByName = memberLoginRepository.findByAccount(createMemberDto.getAccount());
        if (findByName.isPresent()) {
            throw new MemberAlreadyExistException("이미 존재하는 회원입니다.");
        }
    }
}
