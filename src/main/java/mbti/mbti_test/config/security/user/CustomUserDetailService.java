package mbti.mbti_test.config.security.user;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.domain.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberLoginRepository memberLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        //0806 Hayoon
        Member member = memberLoginRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new MemberAdapter(member); // 이런식으로 클래스 만들어줘서 넘겨줘야지 Best practice라고 한다. 독립적인 코드.
    }
}
