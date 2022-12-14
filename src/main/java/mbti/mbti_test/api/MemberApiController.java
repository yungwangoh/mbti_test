package mbti.mbti_test.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mbti.mbti_test.config.security.access.AccessService;
import mbti.mbti_test.config.security.jwt.JwtTokenProvider;
import mbti.mbti_test.config.security.user.CustomUserDetailService;
import mbti.mbti_test.config.security.user.MemberAdapter;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.dto.*;
import mbti.mbti_test.exception.MemberAlreadyExistException;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.impl.MemberServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

//0803 Hayoon
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

    private final AccessService accessService;
    private final Long tokenInvalidTime = 1000L * 60 * 60; // ?????? 1??????

    @GetMapping("/api/v2/members")
    public List<CreateMemberDto> memberV2() {
        List<Member> members = memberService.findMembers();

        return members.stream()
                .map(member -> new CreateMemberDto(member))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    //????????????
    @PostMapping("/api/v3/join")
    public CreateMemberResponse saveMemberV3(@RequestBody CreateMemberDto createMemberDto)
            throws MemberAlreadyExistException {
        Long memberId = memberServiceImpl.join(createMemberDto);

        log.info("\nmemberId: " + memberId + " memberDtoName: " + createMemberDto.getName());
        return new CreateMemberResponse(memberId, createMemberDto.getName());

//        return memberId != null ?
//                ResponseEntity.ok().body("??????????????? ???????????????.") :
//                ResponseEntity.badRequest().build();
    }
    /**
     * {
        "account": "sejong123",
        "pwd": "123456",
        "address": {
            "city": "?????????",
            "street": "?????????",
            "zipcode": "??????3???"
        },
        "email": "abcd1234@naver.com",
        "name": "AAA"
        }
     */

    //?????????
    //0808 Hayoon
    //https://jwt.io/ ?????? Encoder -> Decoder(Payload) ????????????.
    @PostMapping("/api/v3/login")
    public String login(@RequestBody @Valid UserLoginDto userDto) {
        System.out.println(userDto.getAccount() + " " + userDto.getPassword());
        Optional<Member> byAccount = memberLoginRepository.findByAccount(userDto.getAccount());

        if(byAccount.isPresent()) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(byAccount.get().getAccount());
            if (!passwordEncoder.matches(userDto.getPassword(), userDetails.getPassword())) {
                throw new IllegalArgumentException("????????? ?????????????????????.");
            }

            log.info("\n????????? ??????!");
            return jwtTokenProvider.createToken(byAccount.get().getAccount(), byAccount.get().getRoles(), tokenInvalidTime);
        } else throw new IllegalStateException("????????? ????????????.");
    }

    //?????????
    //0820 Hayoon
    //https://jwt.io/ ?????? Encoder -> Decoder(Payload) ????????????.
    //Bearer Token ??????
    @PostMapping("/api/v4/login")
    public ResponseEntity<TokenResponse> loginV4(@RequestBody @Valid UserLoginDto userDto) {
        System.out.println(userDto.getAccount() + " " + userDto.getPassword());
        Member member = memberLoginRepository.findByAccount(userDto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ACCOUNT ?????????."));
        if (!passwordEncoder.matches(userDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("????????? ?????????????????????.");
        }

        log.info("\n????????? ??????!");
        String token = jwtTokenProvider.createToken(member.getUsername(), member.getRoles(), tokenInvalidTime);
        return ResponseEntity.ok().body(new TokenResponse(token, "bearer", member.getAccount()));
    }

    //loginV5 ?????? access, refresh ?????? ?????? Dto??? ??????
    @PostMapping("/api/v5/login")
    public LoginRepositoryDto loginV5(@RequestBody @Valid UserLoginDto userLoginDto) {

        Member member = memberLoginRepository.findByAccount(userLoginDto.getAccount())
                .orElseThrow(() -> new IllegalStateException("???????????? ?????? ACCOUNT ?????????."));
        if(!passwordEncoder.matches(userLoginDto.getPassword(), member.getPassword()))
            throw new IllegalStateException("????????? ?????????????????????.");

        String accessToken = jwtTokenProvider.createAccessToken(member.getAccount(), member.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getAccount(), member.getRoles());
        return new LoginRepositoryDto(accessToken, refreshToken);
    }

    //????????????
    //0821 Hayoon
    @ApiOperation(value ="logout")
    @ApiResponses({@ApiResponse(code = 204, message = "success")})
    @GetMapping("/api/v4/logout")
    public ResponseEntity<?> logout(@RequestHeader("X-AUTH-TOKEN") String jwt) {
        //String token = jwtTokenProvider
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(jwt, jwt);
        MemberAdapter memberAdapter = (MemberAdapter) jwtTokenProvider.getAuthentication(jwt).getPrincipal();
        log.info("???????????? ?????? ?????????: '{}'", memberAdapter.getUsername());
        return new ResponseEntity<>(new DefaultResponseDto( 200,"???????????? ???????????????."), OK);
    }

    //?????? ?????? ????????? ????????????.
    @ApiOperation(value ="logout")
    @ApiResponses({@ApiResponse(code = 204, message = "success")})
    @GetMapping("/api/v5/logout")
    public ResponseEntity<DefaultResponseDto> logout(@RequestParam("account") String account,
                                                     HttpServletRequest httpServletRequest) {
        String accessToken = httpServletRequest.getHeader("X-AUTH-TOKEN"); //accessToken ???
        accessService.logout(account, accessToken);
        DefaultResponseDto response = new DefaultResponseDto(200, "???????????? ??????");
        return new ResponseEntity<>(response, OK);
    }

    //???????????? ??????
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

    //0823 Hayoon
    //?????? ????????? ??????
    @PostMapping("/api/v1/findAccount")
    public ResponseEntity<String> findAccountByEmail(@RequestBody @Valid EmailDto emailDto) {
        Optional<Member> findMemberByEmail = memberLoginRepository.findByEmail(emailDto.getEmail());
        log.info("?????????: " + findMemberByEmail.get().getAccount());
        //throw new IllegalStateException("???????????? ???????????? ????????????.");
        return findMemberByEmail.map(member -> new ResponseEntity<>(member.getAccount(), OK)).
                orElseGet(() -> new ResponseEntity<>("???????????? ???????????? ????????????", NOT_FOUND));
    }

    //0823 Hayoon
    //?????? ???????????? ??????
    @PostMapping("/api/v1/findPassword")
    public ResponseEntity<String> findPwdByAccount(@RequestBody @Valid ChangePwdDto changePwdDto) throws Exception {
        Optional<Member> findMemberByAccount = memberLoginRepository.findByAccount(changePwdDto.getAccount());
        if (findMemberByAccount.isPresent()) {
            if (changePwdDto.getPassword().equals(changePwdDto.getCheckPassword())) {
                memberService.changePwd(findMemberByAccount, changePwdDto);
                return new ResponseEntity<>(changePwdDto.getPassword(), OK);
            } else {
                //throw new IllegalStateException("?????????????????? ??????????????? ????????? ???????????? ????????????.");
                return new ResponseEntity<>("?????????????????? ??????????????? ????????? ???????????? ????????????.", NOT_FOUND);
            }
        } else {
            //throw new IllegalStateException("????????? ???????????? ????????????.");
            return new ResponseEntity<>("????????? ???????????? ????????????.", NOT_FOUND);
        }
    }

    //0810 Hayoon
    //User ??? ?????? ????????? ????????? ???????????? ????????? ?????? ?????? Response ??????
    @PostMapping("/api/user/test")
    public Map userResponseTest() {
        Map<String, String> result = new HashMap<>();
        result.put("result", "user ok");
        return result;
    }
    //0810 Hayoon
    //Admin ??? ?????? ????????? ????????? ???????????? ????????? ?????? ?????? Response ??????
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

