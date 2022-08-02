package mbti.mbti_test.repository;

import mbti.mbti_test.domain.Member;

import java.util.List;

public interface MemberRepository {

    void save(Member member);
    Member findOne(Long id);
    List<Member> findAll();
    List<Member> findByName(String name);
}
