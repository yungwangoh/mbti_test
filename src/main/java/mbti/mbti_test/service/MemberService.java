package mbti.mbti_test.service;

import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.dto.ChangePwdDto;
import mbti.mbti_test.dto.CreateMemberDto;
import mbti.mbti_test.dto.UpdateMemberDto;
import mbti.mbti_test.exception.MemberAlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    //Long join(Member member) throws MemberAlreadyExistException;
    Member findOne(Long memberId);
    List<Member> findMembers();
    void updateMember(Optional<Member> findMember, UpdateMemberDto updateMemberDto);
    void userValidation(CreateMemberDto createMemberDto) throws MemberAlreadyExistException;
    void changePwd(Optional<Member> findMember, ChangePwdDto changePwdDto) throws Exception;
}
