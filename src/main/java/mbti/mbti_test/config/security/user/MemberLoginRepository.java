package mbti.mbti_test.config.security.user;

import mbti.mbti_test.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberLoginRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAccount(String account);

    //0823 Hayoon
    // 이메일로 아이디 찾기
    @Query("select m from Member m where m.email = :email")
    Optional<Member> findByEmail(@Param("email") String email);
}
