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

    private final ResultService resultService;

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
        String icString = whaleAlgorithm.ieSelect(iCount, eCount);
        String snString = whaleAlgorithm.snSelect(sCount, nCount);
        String tfString = whaleAlgorithm.tfSelect(tCount, fCount);
        String pjString = whaleAlgorithm.pjSelect(pCount, jCount);

        return resultService.mbtiChangeEnum(whaleAlgorithm.mbtiCombination(icString, snString, tfString, pjString));
    }

    @PostMapping("/api/create/result")
    public CreateResultResponse saveResultV2(@RequestBody @Valid CreateResultDto createResultDto) {
        Result result = Result.createResult(createResultDto.getMember(), createResultDto.getMbtiList(), createResultDto.getWhaleCount());
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
