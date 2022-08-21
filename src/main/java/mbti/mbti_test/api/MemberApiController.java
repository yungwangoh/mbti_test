package mbti.mbti_test.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mbti.mbti_test.config.security.user.CustomUserDetailService;
import mbti.mbti_test.config.security.user.MemberAdapter;
import mbti.mbti_test.dto.CreateMemberDto;
import mbti.mbti_test.dto.TokenDto;
import mbti.mbti_test.dto.UpdateMemberDto;
import mbti.mbti_test.service.impl.MemberServiceImpl;
import mbti.mbti_test.dto.UserLoginDto;
import mbti.mbti_test.exception.MemberAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import mbti.mbti_test.config.security.JwtTokenProvider;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//0803 hayoon
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberLoginRepository memberLoginRepository;
    private final MemberServiceImpl memberServiceImpl;
    private final CustomUserDetailService customUserDetailService;
    private final StringRedisTemplate redisTemplate;

    @GetMapping("/api/v2/members")
    public List<CreateMemberDto> memberV2() {
        List<Member> members = memberService.findMembers();

        List<CreateMemberDto> createMemberDtos = members.stream()
                .map(member -> new CreateMemberDto(member))
                .collect(Collectors.toList());

        return createMemberDtos;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    //회원가입
    @PostMapping("/api/v3/join")
    public CreateMemberResponse saveMemberV3(@RequestBody CreateMemberDto createMemberDto)
            throws MemberAlreadyExistException {
        Long memberId = memberServiceImpl.join(createMemberDto);

        log.info("\nmemberId: " + memberId + " memberDtoName: " + createMemberDto.getName());
        return new CreateMemberResponse(memberId, createMemberDto.getName());

//        return memberId != null ?
//                ResponseEntity.ok().body("회원가입을 축하합니다.") :
//                ResponseEntity.badRequest().build();
    }
    /**
     * {
        "account": "sejong123",
        "pwd": "123456",
        "address": {
            "city": "서울시",
            "street": "광진구",
            "zipcode": "세종3로"
        },
        "email": "abcd1234@naver.com",
        "name": "AAA"
        }
     * @param userDto
     * @return
     */

    // 로그인
    //0808 Hayoon
    //https://jwt.io/ 에서 Encoder -> Decoder(Payload) 확인가능.
    @PostMapping("/api/v3/login")
    public String login(@RequestBody @Valid UserLoginDto userDto) {
        System.out.println(userDto.getAccount() + " " + userDto.getPassword());
        Optional<Member> byAccount = memberLoginRepository.findByAccount(userDto.getAccount());

        if(byAccount.isPresent()) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(byAccount.get().getAccount());
            if (!passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            log.info("\n로그인 성공!");
            return jwtTokenProvider.createToken(userDetails.getUsername(), byAccount.get().getRoles());
        } else throw new IllegalStateException("계정이 없습니다.");
        log.info("\n로그인 성공!");
        String token = jwtTokenProvider.createToken(byAccount.get().getUsername(), byAccount.get().getRoles());
        log.info("\n토큰 복호화->Account" + jwtTokenProvider.getMemberPk(token));
        return jwtTokenProvider.createToken(userDetails.getUsername(), byAccount.get().getRoles());
    }

    // 로그인
    //0820 Hayoon
    //https://jwt.io/ 에서 Encoder -> Decoder(Payload) 확인가능.
    //Bearer Token 생성
    @PostMapping("/api/v4/login")
    public ResponseEntity<TokenResponse> loginV4(@RequestBody @Valid UserLoginDto userDto) {
        System.out.println(userDto.getAccount() + " " + userDto.getPassword());
        Member member = memberLoginRepository.findByAccount(userDto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ACCOUNT 입니다."));
        if (!passwordEncoder.matches(userDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        log.info("\n로그인 성공!");
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer", member.getAccount()));
    }

    // 로그아웃
    //0821 Hayoon
    @ApiOperation(value ="logout")
    @ApiResponses({@ApiResponse(code = 204, message = "success")})
    @GetMapping("/api/v4/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-AUTH-TOKEN") String jwt) {
        //String token = jwtTokenProvider
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(jwt, jwt);
        MemberAdapter memberAdapter = (MemberAdapter) jwtTokenProvider.getAuthentication(jwt).getPrincipal();
        log.info("로그아웃 유저 아이디: '{}'", memberAdapter.getUsername());
        return new ResponseEntity<>(new DefaultResponseDto( 200,"로그아웃 되었습니다."), HttpStatus.OK);
    }

    // 회원정보 수정
    //0813 Hayoon
    @PutMapping("/api/v2/members")
    public UpdateMemberResponse updateMemberV2(@RequestHeader("X-AUTH-TOKEN") String token,
                                               @RequestBody @Valid UpdateMemberDto updateMemberDto) {
        String account = jwtTokenProvider.getMemberPk(token);
        Optional<Member> findMember = memberLoginRepository.findByAccount(account);
        memberService.updateMember(findMember, updateMemberDto);

        return new UpdateMemberResponse(findMember.get().getEmail(),
                findMember.get().getAddress(), findMember.get().getAccount(), findMember.get().getPwd(),
                findMember.get().getUpdateDateTime());
    }

    /*
     * AccessToken이 만료되었을 때 토큰(AccessToken , RefreshToken) 재발급해주는 메서드
     */
//    @PostMapping("/reissue")
//    public ResponseEntity<TokenDto> reissue(
//            @RequestBody @Valid TokenDto requestTokenDto) {
//        TokenDto tokenDto = authService
//                .reissue(requestTokenDto.getAccessToken(), requestTokenDto.getRefreshToken());
//        return ResponseEntity.ok(new TokenDto());
//    }

    //0810 Hayoon
    //User 외 접근 제한을 걸어둔 리소스에 요청을 보내 결과 Response 확인
    @PostMapping("/api/user/test")
    public Map userResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "user ok");
        return result;
    }
    //0810 Hayoon
    //Admin 외 접근 제한을 걸어둔 리소스에 요청을 보내 결과 Response 확인
    @PostMapping("/api/admin/test")
    public Map adminResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "admin ok");
        return result;
    }
    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CreateMemberResponse {
        private Long createId;
        private String name;

        public CreateMemberResponse(Long createId, String name) {
            this.createId = createId;
            this.name = name;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class UpdateMemberResponse {
        private String email;
        private Address address;
        private String account;
        private String pwd;
        private LocalDateTime updateDateTime;

        public UpdateMemberResponse(String email, Address address,
                                    String account, String pwd,
                                    LocalDateTime updateDateTime) {
            this.email = email;
            this.address = address;
            this.account = account;
            this.pwd = pwd;
            this.updateDateTime = updateDateTime;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class TokenResponse {
        private String accessToken;
        private String tokenType;
        private String account;

        public TokenResponse(String accessToken, String tokenType, String account) {
            this.accessToken = accessToken;
            this.tokenType = tokenType;
            this.account = account;
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class DefaultResponseDto {
        private int serverStatus;
        private String msg;

        public DefaultResponseDto(int serverStatus, String msg) {
            this.serverStatus = serverStatus;
            this.msg = msg;
        }
    }

}

