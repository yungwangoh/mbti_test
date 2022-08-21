package mbti.mbti_test.api;

import lombok.RequiredArgsConstructor;
import mbti.mbti_test.dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.service.WhaleCountService;
import mbti.mbti_test.service.impl.WhaleAlgorithm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class WhaleCountApiController { //0805 ygo 생성

    private final WhaleCountService whaleCountService;
    private final WhaleAlgorithm whaleAlgorithm;

    @GetMapping("/api/v2/whale-lists")
    public List<CreateWhaleCountDto> whaleCountListV2() {

        List<WhaleCount> whaleList = whaleCountService.findAll();

        List<CreateWhaleCountDto> whaleCountDtos = getCreateWhaleCountDtos(whaleList);

        return whaleCountDtos;
    }

    @GetMapping("/api/whila-list/share")
    public List<CreateWhaleCountDto> whaleCountListShare() {

        whaleAlgorithm.AllSharePoints(whaleCountService.findAll());

        List<WhaleCount> whaleCountList = whaleCountService.findAll();

        List<CreateWhaleCountDto> whaleCountDtos = getCreateWhaleCountDtos(whaleCountList);

        return whaleCountDtos;
    }

    @GetMapping("/api/sort/whale-descending")
    public List<CreateWhaleCountDto> whaleDescending() {
        List<WhaleCount> whaleSortDescend = whaleCountService.whaleSortDescend();

        List<CreateWhaleCountDto> whaleCountDtos = getCreateWhaleCountDtos(whaleSortDescend);

        return whaleCountDtos;
    }

    @GetMapping("/api/list/max-whale")
    public List<CreateWhaleCountDto> whaleMaxWhale() {

        List<WhaleCount> whaleCounts = whaleCountService.maxWhaleNameShare();

        List<CreateWhaleCountDto> whaleCountDtos = getCreateWhaleCountDtos(whaleCounts);

        return whaleCountDtos;
    }

    @GetMapping("/api/list/min-whale")
    public List<CreateWhaleCountDto> whaleMinWhale() {

        List<WhaleCount> whaleCounts = whaleCountService.minWhaleNameShare();

        List<CreateWhaleCountDto> whaleCountDtos = getCreateWhaleCountDtos(whaleCounts);

        return whaleCountDtos;
    }

    @PostMapping("/api/whale-compatibility")
    public List<CreateWhaleCountDto> whaleCompatibilityApi(@RequestBody @Valid CreateWhaleCountDto createWhaleCountDto) {
        List<WhaleCount> whaleCounts = whaleCountService.whaleCompatibility(createWhaleCountDto);

        List<CreateWhaleCountDto> whaleCountDtos = getCreateWhaleCountDtos(whaleCounts);

        return whaleCountDtos;
    }

    private List<CreateWhaleCountDto> getCreateWhaleCountDtos(List<WhaleCount> whaleList) {
        List<CreateWhaleCountDto> whaleCountDtos = whaleList.stream()
                .map(whaleCount -> new CreateWhaleCountDto(whaleCount))
                .collect(toList());
        return whaleCountDtos;
    }
}
