package mbti.mbti_test.service;

import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.WhaleCount;

import java.util.List;

public interface MemberService {

    Long join(Member member);
    Member findOne(Long memberId);
    List<Member> findMembers();

}
