package mbti.mbti_test.config.security.user;

import lombok.RequiredArgsConstructor;
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
        return memberLoginRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    }
}
