package mbti.mbti_test.config.security.user;

import mbti.mbti_test.domain.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultCountRepository extends JpaRepository<Result, Long> {

    List<Result> findFirstByOrderByIdDesc();
}
