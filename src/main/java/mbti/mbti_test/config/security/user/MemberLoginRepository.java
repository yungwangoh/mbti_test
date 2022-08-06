package mbti.mbti_test.config.security.user;

import mbti.mbti_test.domain.Member;
import mbti.mbti_test.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLoginRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAccount(String account);
}
