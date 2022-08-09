package mbti.mbti_test.api;

import lombok.*;
import mbti.mbti_test.Dto.CreateMemberDto;
import mbti.mbti_test.Dto.CreateResultDto;
import mbti.mbti_test.Dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.MbtiList;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.Result;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.repository.ResultRepository;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import mbti.mbti_test.service.WhaleCountService;
import mbti.mbti_test.service.impl.WhaleAlgorithm;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class ResultApiController {

    private final MemberService memberService;
    private final ResultService resultService;
    private final WhaleCountService whaleCountService;

    @GetMapping("/api/v2/result")
    public List<CreateResultDto> resultV2() {
        List<Result> results = resultService.resultAll();

        List<CreateResultDto> resultDtos = results.stream()
                .map(result -> new CreateResultDto(result))
                .collect(toList());

        return resultDtos;
    }

    @GetMapping("/api/v3/result")
    public List<CreateResultDto> resultV3() { // fetch join으로 최적화.
        List<Result> results = resultService.findWithMemberWhale();

        List<CreateResultDto> resultDtos = results.stream()
                .map(result -> new CreateResultDto(result))
                .collect(toList());

        return resultDtos;
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

        List<CreateWhaleCountDto> whaleCountDtos = whaleCounts.stream()
                .map(whaleCount -> new CreateWhaleCountDto(whaleCount))
                .collect(toList());

        return whaleCountDtos;
    }

    @PostMapping("/api/history/result")
    public List<CreateWhaleCountDto> userResultHistory(@RequestBody @Valid CreateMemberDto createMemberDto) {

        List<WhaleCount> whaleCounts = new ArrayList<>();
        List<Result> memberResultService = resultService.findMemberResultService(createMemberDto.getId());

        memberResultService.forEach(result -> {
            whaleCounts.add(result.getWhaleCount());
        });

        List<CreateWhaleCountDto> whaleCountDtos = whaleCounts.stream()
                .map(whaleCount -> new CreateWhaleCountDto(whaleCount))
                .collect(toList());

        return whaleCountDtos;
    }

    @PostMapping("/api/create/result")
    public CreateResultResponse saveResultV2(@RequestBody @Valid CreateResultSave createResultSave) {
        Member findMember = memberService.findOne(createResultSave.createMemberDto.getId());
        WhaleCount whaleNameMbti = whaleCountService.findWhaleNameMbti(createResultSave.createWhaleCountDto.getWhaleName());
        Result result = Result.createResult(findMember, whaleNameMbti);
        Long crateResultId = resultService.ResultJoin(result);

        return new CreateResultResponse(crateResultId);
    }

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class CreateResultSave {
        private CreateMemberDto createMemberDto;
        private CreateWhaleCountDto createWhaleCountDto;

        public CreateResultSave(CreateMemberDto createMemberDto, CreateWhaleCountDto createWhaleCountDto) {
            this.createMemberDto = createMemberDto;
            this.createWhaleCountDto = createWhaleCountDto;
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
