package mbti.mbti_test.config.security.access;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.config.security.jwt.JwtTokenProvider;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.dto.LoginRepositoryDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccessService { // access token 서비스.

    private final MemberLoginRepository memberLoginRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /*  토큰 재발급
    1. 전달받은 유저의 아이디로 유저가 존재하는지 확인한다.
    2. RefreshToken이 유효한지 체크한다.
    3. AccessToken을 발급하여 기존 RefreshToken과 함께 응답한다.
     */
    public LoginRepositoryDto reIssueAccessToken(String account, String refreshToken) {
        Member member = memberLoginRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 유저입니다."));
        String accessToken = jwtTokenProvider.createAccessToken(member.getAccount(), member.getRoles());
        return new LoginRepositoryDto(accessToken, refreshToken);
    }

    // 토큰 로그아웃 서비스
    public void logout(String account, String accessToken) {
        jwtTokenProvider.logout(account, accessToken);
    }
}
