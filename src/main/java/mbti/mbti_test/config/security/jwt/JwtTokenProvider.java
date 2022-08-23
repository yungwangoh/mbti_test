package mbti.mbti_test.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mbti.mbti_test.redis.RedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    private final StringRedisTemplate redisTemplate;

    private String secretKey = "mbti";

    private String blackList = "blackList";

    //토큰 유효시간 60분
    private final long tokenValidTime = 60 * 60 * 1000L; // 1시간만 토큰 유효

    private final UserDetailsService userDetailsService;

    private final RedisService redisService;

    //객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // access token 생성
    public String createAccessToken(String account, List<String> userRole) {
        Long tokenInvalidTime = 1000L * 60 * 3; // Hayoon 3분 ?
        return this.createToken(account, userRole, tokenInvalidTime);
    }

    // refresh token 생성
    public String createRefreshToken(String account, List<String> userRole) {
        Long tokenInvalidTime = 1000L * 60 * 60 * 24; // Hayoon 24시간
        String refreshToken = this.createToken(account, userRole, tokenInvalidTime);
        redisService.setValues(account, refreshToken, Duration.ofMillis(tokenInvalidTime));
        return refreshToken;
    }

    public void checkRefreshToken(String account, String refreshToken) {
        String redisRT = redisService.getValues(account);
        if(!refreshToken.equals(redisRT)) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }
    }

    public void logout(String account, String accessToken) {
        long expiredAccessTokenTime = getExpiredTime(accessToken).getTime() - new Date().getTime();
        redisService.setValues(blackList + accessToken, account, Duration.ofMillis(expiredAccessTokenTime));
        redisService.deleteValues(account); // Redis에서 유저 리프레시 토큰 삭제
    }

    //JWT 토큰 생성
    // 구글링 예제와 맞추기 위해 파라미터 하나 추가 (tokenValidTime) 만료 시간
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

    // 토큰 만료시간 구하기
    private Date getExpiredTime(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }
}
