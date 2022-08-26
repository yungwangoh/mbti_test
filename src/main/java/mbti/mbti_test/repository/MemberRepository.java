package mbti.mbti_test.repository;

import mbti.mbti_test.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    void save(Member member);
    Member findOne(Long id);
    List<Member> findAll();
    List<Member> findByName(String name);

    Optional<Member> findByEmail(String email);
}
