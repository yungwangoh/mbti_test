package mbti.mbti_test.config.security.user;

import mbti.mbti_test.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLoginRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAccount(String account);

    //0823 Hayoon
    // 이메일로 아이디 찾기
    Optional<Member> findByEmail(String email);
}
