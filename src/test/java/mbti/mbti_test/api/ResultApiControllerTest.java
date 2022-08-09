package mbti.mbti_test.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mbti.mbti_test.Dto.CreateMemberDto;
import mbti.mbti_test.Dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import mbti.mbti_test.service.WhaleCountService;
import mbti.mbti_test.service.impl.WhaleAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ResultApiControllerTest {

    @Autowired
    ResultService resultService;

    @Autowired
    MemberService memberService;

    @Autowired
    WhaleCountService whaleCountService;

    @Autowired
    ResultApiController resultApiController;

    @Test
    public void jsonTest() throws JsonProcessingException { // 객체를 Json 형식으로 Convert;
        Address address = new Address("인천시", "문화로", "123");
        Member member = new Member("윤광오", "qkfks1234", "1234",
                address, "swager253@naver.com", MemberStatus.USER);

        WhaleCount whaleCount = whaleCountService.findWhaleNameMbti("범고래");

        CreateWhaleCountDto createWhaleCountDto = new CreateWhaleCountDto(whaleCount);

        Member findMember = memberService.findOne(17L);
        CreateMemberDto createMemberDto = new CreateMemberDto(findMember);

        ResultApiController.CreateResultSave resultApiController = new ResultApiController
                .CreateResultSave(new CreateMemberDto(member), new CreateWhaleCountDto(whaleCount));


        ObjectMapper mapper = new ObjectMapper();

        // writeValueAsString에 파라미터로 객체를 넣으면 된다.
        String s = mapper.registerModule(new JavaTimeModule()).writeValueAsString(resultApiController);

        // 출력
        System.out.println("mapper = " + s);
    }
}