package mbti.mbti_test.api;

import lombok.*;
import mbti.mbti_test.config.security.JwtTokenProvider;
import mbti.mbti_test.config.security.user.CustomUserDetailService;
import mbti.mbti_test.config.security.user.MemberLoginRepository;
import mbti.mbti_test.dto.CreateResultDto;
import mbti.mbti_test.dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.Result;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import mbti.mbti_test.service.WhaleCountService;
import mbti.mbti_test.service.impl.WhaleAlgorithm;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class ResultApiController {

    private final MemberService memberService;
    private final MemberLoginRepository memberLoginRepository;
    private final ResultService resultService;
    private final WhaleCountService whaleCountService;

    private final CustomUserDetailService findByAccount;
    private final WhaleAlgorithm whaleAlgorithm;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/v2/result")
    public List<CreateResultDto> resultV2() {
        List<Result> results = resultService.resultAll();

        return results.stream()
                .map(result -> new CreateResultDto(result))
                .collect(toList());
    }

    @GetMapping("/api/v3/result")
    public List<CreateResultDto> resultV3() { // fetch join으로 최적화.
        List<Result> results = resultService.findWithMemberWhale();

        return results.stream()
                .map(result -> new CreateResultDto(result))
                .collect(toList());
    }

    @PostMapping("/api/algorithm/result")
    public List<CreateWhaleCountDto> changeMbtiEnumToString(@RequestBody @Valid WhaleAlgorithm whaleAlgorithm) {

        WhaleAlgorithm algorithm = new WhaleAlgorithm(whaleAlgorithm);

        //0806 Hayoon
        //isString으로 되어있어서 ieString으로 변수명 변경함.
        String ieString = algorithm.ieSelect();
        String snString = algorithm.snSelect();
        String tfString = algorithm.tfSelect();
        String pjString = algorithm.pjSelect();

        String whaleName = resultService.mbtiChangeEnum(algorithm.mbtiCombination(ieString, snString, tfString, pjString))
                .whaleNameMethod();

        List<WhaleCount> whaleCounts = new ArrayList<>();
        whaleCounts.add(whaleCountService.findWhaleNameMbti(whaleName));

        return whaleCounts.stream()
                .map(whaleCount -> new CreateWhaleCountDto(whaleCount))
                .collect(toList());
    }

    // 0818 리팩토링
    @PostMapping("/api/history/result")
    public List<CreateResultHistory> userResultHistory(@RequestHeader("X-AUTH-TOKEN") String token) {

        List<Result> resultListHistory = new ArrayList<>();
        String memberPk = jwtTokenProvider.getMemberPk(token);
        Optional<Member> account = memberLoginRepository.findByAccount(memberPk);

        if(account.isPresent()) { // History 조회는 회원만 가능하다.

            List<Result> memberResultService = resultService.findMemberResultService(account.get().getId());

            resultListHistory.addAll(memberResultService);

            return resultListHistory.stream()
                    .map(result -> new CreateResultHistory(result))
                    .collect(toList());
        } else throw new IllegalAccessError("비회원은 접근이 불가능합니다."); // 비회원 Error..
    }

    @PostMapping("/api/create/user/result") // 회원 결과 저장
    public CreateResultResponse saveResultV2User(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody @Valid CreateWhaleCountDto createWhaleCountDto) {
        String memberPk = jwtTokenProvider.getMemberPk(token);
        Optional<Member> account = memberLoginRepository.findByAccount(memberPk);

        if(!account.isEmpty()) { // 회원일 경우
            WhaleCount whaleNameMbti = whaleCountService.findWhaleNameMbti(createWhaleCountDto.getWhaleName());
            Result result = Result.createResult(account.get(), whaleNameMbti);
            whaleAlgorithm.AllSharePoints(whaleCountService.findAll());

            Long crateResultId = resultService.ResultJoin(result);

            return new CreateResultResponse(crateResultId);
        }else throw new IllegalAccessError("비회원은 접근이 불가합니다.");
    }

    @PostMapping("/api/create/non-user/result") // 비회원 결과 저장
    public void saveResultV2NonUser(@RequestBody @Valid CreateWhaleCountDto createWhaleCountDto) {
        WhaleCount whaleNameMbti = whaleCountService.findWhaleNameMbti(createWhaleCountDto.getWhaleName());
        whaleNameMbti.whaleCountValue();
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CreateResultHistory {

        private LocalDateTime resultTestTime;
        private CreateWhaleCountDto whaleCountDto;

        public CreateResultHistory(Result result) {
            this.resultTestTime = result.getTestTime();
            this.whaleCountDto = new CreateWhaleCountDto(result.getWhaleCount());
        }
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CreateResultResponse {
        private Long resultId;

        public CreateResultResponse(Long resultId) {
            this.resultId = resultId;
        }
    }
}
