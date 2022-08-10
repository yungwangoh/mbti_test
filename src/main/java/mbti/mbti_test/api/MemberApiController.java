package mbti.mbti_test.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mbti.mbti_test.config.security.UserService;
import mbti.mbti_test.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import mbti.mbti_test.config.security.JwtTokenProvider;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.dto.CreateMemberDto;
import mbti.mbti_test.dto.UpdateMemberDto;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.service.MemberService;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//0803 hayoon
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberLoginRepository memberLoginRepository;
    private final UserService userService;


    @GetMapping("/api/v2/members")
    public List<CreateMemberDto> memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<CreateMemberDto> memberDtos = findMembers.stream()
                .map(m -> new CreateMemberDto(m))
                .collect(Collectors.toList());
        return memberDtos;
    }

    //0808 Hayoon
    //Data JPA 미구현.
    @GetMapping("/api/v3/members")
    public List<Member> findAllUser() {
        return memberLoginRepository.findAll();
    }

    @GetMapping("/api/v2.1/members")
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
    public ResponseEntity saveMemberV3(@RequestBody CreateMemberDto createMemberDto) {
        Long memberId = userService.join(createMemberDto);
        return memberId != null ?
                ResponseEntity.ok().body("회원가입을 축하합니다.") :
                ResponseEntity.badRequest().build();
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
    public String login(@RequestBody UserDto userDto) {
        System.out.println(userDto.getAccount() + " " + userDto.getPassword());
        Member member = memberLoginRepository.findByAccount(userDto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 ACCOUNT 입니다."));
        if (!passwordEncoder.matches(userDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        ResponseEntity.ok().body("로그인 성공!.");
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }


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
    static class CreateMemberResponse {
        private Long createId;
        public CreateMemberResponse(Long id) {
            this.createId = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private String email;
        private Address address;
        private String account;
        private String pwd;
        private LocalDateTime updateDateTime;
    }
}

