package mbti.mbti_test.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/api/algorithm/result")
    public MbtiList changeMbtiEnumToString(@RequestParam(value = "iCount") int iCount, @RequestParam(value = "eCount") int eCount,
                                          @RequestParam(value = "sCount") int sCount, @RequestParam(value = "nCount") int nCount,
                                          @RequestParam(value = "tCount") int tCount, @RequestParam(value = "fCount") int fCount,
                                          @RequestParam(value = "pCount") int pCount, @RequestParam(value = "jCount") int jCount) {

        WhaleAlgorithm whaleAlgorithm = new WhaleAlgorithm(iCount, eCount, sCount, nCount, tCount, fCount, pCount, jCount);
        //0806 Hayoon
        //isString으로 되어있어서 ieString으로 변수명 변경함.
        String ieString = whaleAlgorithm.ieSelect(iCount, eCount);
        String snString = whaleAlgorithm.snSelect(sCount, nCount);
        String tfString = whaleAlgorithm.tfSelect(tCount, fCount);
        String pjString = whaleAlgorithm.pjSelect(pCount, jCount);

        return resultService.mbtiChangeEnum(whaleAlgorithm.mbtiCombination(ieString, snString, tfString, pjString));
    }

    @GetMapping("/api/v3/user-results")
    public List<CreateWhaleCountDto> userResult(@RequestParam("memberId") Long memberId) {

        List<WhaleCount> whaleCounts = new ArrayList<>();
        List<Result> memberResultService = resultService.findMemberResultService(memberId);

        for(Result result : memberResultService) {
            whaleCounts.add(result.getWhaleCount());
        }

        List<CreateWhaleCountDto> whaleCountDtos = whaleCounts.stream()
                .map(whaleCount -> new CreateWhaleCountDto(whaleCount))
                .collect(toList());

        return whaleCountDtos;
    }

    @PostMapping("/api/create/result")
    public CreateResultResponse saveResultV2(@RequestBody @Valid CreateMemberDto createMemberDto,
                                             @RequestBody @Valid MbtiList mbtiList) {

        Member findMember = memberService.findOne(createMemberDto.getId());
        WhaleCount whaleNameMbti = whaleCountService.findWhaleNameMbti(mbtiList.whaleNameMethod());
        Result result = Result.createResult(findMember, mbtiList, whaleNameMbti);
        Long crateResultId = resultService.ResultJoin(result);

        return new CreateResultResponse(crateResultId);
    }

    @Data
    static class CreateResultResponse {
        private Long resultId;

        public CreateResultResponse(Long resultId) {
            this.resultId = resultId;
        }
    }
}
