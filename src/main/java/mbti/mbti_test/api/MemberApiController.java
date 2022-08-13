package mbti.mbti_test.api;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mbti.mbti_test.dto.CreateMemberDto;
import mbti.mbti_test.dto.UpdateMemberDto;
import mbti.mbti_test.service.impl.MemberServiceImpl;
import mbti.mbti_test.dto.UserLoginDto;
import mbti.mbti_test.exception.MemberAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import mbti.mbti_test.config.security.JwtTokenProvider;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.service.MemberService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/v2/members")
    public List<CreateMemberDto> memberV2_1() {
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
    public String login(@RequestBody UserLoginDto userDto) {
        System.out.println(userDto.getAccount() + " " + userDto.getPassword());
        Member member = memberLoginRepository.findByAccount(userDto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ACCOUNT 입니다."));
        if (!passwordEncoder.matches(userDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        log.info("로그인 성공!");
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    // 회원정보 수정
    //0813 Hayoon
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberDto updateMemberDto) {
        Member findMember = memberService.findOne(id);
        memberService.updateMember(findMember, updateMemberDto);
        return new UpdateMemberResponse(findMember.getEmail(),
                findMember.getAddress(), findMember.getAccount(), findMember.getPwd(),
                findMember.getUpdateDateTime());
    }

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
}

