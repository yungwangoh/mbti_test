package mbti.mbti_test.config.security.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.dto.LoginRepositoryDto;
import mbti.mbti_test.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    private final StringRedisTemplate redisTemplate;

    private String secretKey = "mbti";

    private String blackList = "blackList";

    private final UserDetailsService userDetailsService;

    private final MemberLoginRepository memberLoginRepository;

    private final RedisService redisService;


    //객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // access token 생성
    public String createAccessToken(String account, List<String> userRole) {
        Long tokenInvalidTime = 1000L * 60 * 3; // Hayoon 30분
        return this.createToken(account, userRole, tokenInvalidTime);
    }

    // refresh token 생성
    public String createRefreshToken(String account, List<String> userRole) {
        Long tokenInvalidTime = 1000L * 60 * 60 * 24 ; // Hayoon 1일
        String refreshToken = this.createToken(account, userRole, tokenInvalidTime);
        redisService.setValues(account, refreshToken, Duration.ofMillis(tokenInvalidTime));
        return refreshToken;
    }

    public void checkRefreshToken(String account, String refreshToken) { // refreshToken 유효성 검사
        String redisRT = redisService.getValues(account);
        if (!refreshToken.equals(redisRT))
            throw new ResponseStatusException(UNAUTHORIZED); // expired refresh-token : 401 unauthorized, login redirection.
    }

    // 0829 accessToken 만료 확인 -> 리프레쉬 없으면 로그인 리디렉션
    // 구체적인 설명 : https://kukekyakya.tistory.com/entry/Spring-boot-access-token-refresh-token-%EB%B0%9C%EA%B8%89%EB%B0%9B%EA%B8%B0jwt
    public boolean checkAccessToken(String accessToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public void logout(String account, String accessToken) {
        long expiredAccessTokenTime = getExpiredTime(accessToken).getTime() - new Date().getTime();
        redisService.setValues(blackList + accessToken, account, Duration.ofMillis(expiredAccessTokenTime));
        redisService.deleteValues(account); // Redis에서 유저 리프레시 토큰 삭제
    }

    //JWT 토큰 생성
    //구글링 예제와 맞추기 위해 파라미터 하나 추가 (tokenValidTime) 만료 시간
    //0825 Hayoon Token이 복호화가 되지가 않음. 파라미터 문제 ?
    public String createToken(String memberPk, List<String> roles, Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(memberPk); //JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); //<key, Value> 쌍으로 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime)) //set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey) // 사용할 암호화 알고리즘과 signature 에 들어갈 secret 값 세팅
                .compact();
    }

    //JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getMemberPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원 정보 추출
    // return : account
    public String getMemberPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    //토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
            if(logoutValueOperations.get(jwtToken) != null) {
                log.info("로그아웃 된 토큰입니다.");
                return false;
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 엑세스 토큰 만료시간 구하기
    private Date getExpiredTime(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }
}
