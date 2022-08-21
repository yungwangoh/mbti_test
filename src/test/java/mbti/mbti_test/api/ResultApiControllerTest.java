package mbti.mbti_test.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mbti.mbti_test.config.security.user.MemberAdapter;
import mbti.mbti_test.dto.CreateMemberDto;
import mbti.mbti_test.dto.CreateWhaleCountDto;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;
import mbti.mbti_test.domain.WhaleCount;
import mbti.mbti_test.service.MemberService;
import mbti.mbti_test.service.ResultService;
import mbti.mbti_test.service.WhaleCountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        ObjectMapper mapper = new ObjectMapper();

        // writeValueAsString에 파라미터로 객체를 넣으면 된다.
        String s = mapper.registerModule(new JavaTimeModule()).writeValueAsString(resultApiController);

        // 출력
        System.out.println("mapper = " + s);
    }
}