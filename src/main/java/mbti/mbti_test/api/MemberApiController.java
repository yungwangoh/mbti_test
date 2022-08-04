package mbti.mbti_test.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import mbti.mbti_test.Dto.CreateMemberDto;
import mbti.mbti_test.Dto.UpdateMemberDto;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//0803 hayoon
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<CreateMemberDto> collect = findMembers.stream()
                .map(m -> new CreateMemberDto(m.getName(), m.getAccount(), m.getPwd(),
                        m.getAddress(), m.getEmail(), m.getMemberStatus(), m.getCreateDateTime()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberDto createMemberDto) {

        Member member = createMember(createMemberDto);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    private Member createMember(CreateMemberDto createMemberDto) {
        Member member = new Member(createMemberDto.getName(), createMemberDto.getAccount(), createMemberDto.getPwd(),
                createMemberDto.getAddress(), createMemberDto.getEmail(), createMemberDto.getMemberStatus());

        return member;
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberDto updateMemberDto) {
        Member findMember = memberService.findOne(id);
        findMember.updateMember(updateMemberDto.getAccount(), updateMemberDto.getPwd(), updateMemberDto.getAddress(),
                updateMemberDto.getEmail(), updateMemberDto.getUpdateDateTime());
        return new UpdateMemberResponse(findMember.getEmail(),
                findMember.getAddress(), findMember.getAccount(), findMember.getPwd(),
                findMember.getUpdateDateTime());
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

