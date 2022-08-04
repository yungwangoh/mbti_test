package mbti.mbti_test.Dto;

import lombok.Data;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

//0803 hayoon
@Data
public class CreateMemberDto {

    @NotEmpty(message = "회원 이름은 필수입니다.") // Dto에 이거 넣으면 안됨 (@NotEmpty). 검증은 따로 클래스 만들어야함
    private String name;

    private String email;

    @Embedded // 이것도 Dto를 따로 생성 도메인 클래스마다 Dto를 생성해야함. @Embedded 안넣어도 될듯? GetAddress()로 넘기는게 좋아보임.
    private Address address;

    @NotEmpty(message = "아이디는 필수입니다.") // Dto에 이거 넣으면 안됨 (@NotEmpty). 검증은 따로 클래스 만들어야함
    private String account;

    @NotEmpty(message = "비밀번호는 필수입니다.") // Dto에 이거 넣으면 안됨 (@NotEmpty). 검증은 따로 클래스 만들어야함
    private String pwd;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus; // 유저와 비유저 [USER, NONUSER]

    private Long creatorId;
    private LocalDateTime createdDateTime;
    //private String updateId;
    //private String updateDateTime;
    //private List<MemberFileDto> fileList;


    public CreateMemberDto(String name, String email, Address address,
                           String account, String pwd, MemberStatus memberStatus,
                           LocalDateTime createdDateTime) {
        this.name = name;
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.memberStatus = memberStatus;
        this.createdDateTime = createdDateTime;
    }

    public CreateMemberDto(String name, String account, String pwd,
                           Address address, String email,
                           MemberStatus memberStatus, LocalDateTime createDateTime) { // 객체로 파라미터로 넘기는게 더 좋아보임.
        this.name = name;
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.memberStatus = memberStatus;
        this.createdDateTime = createDateTime;
    }

    public CreateMemberDto(Member member) { // createResultDto에 쓰일 CreateMemberDto 생성 0804.
        this.name = member.getName();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.account = member.getAccount();
        this.pwd = member.getPwd();
        this.memberStatus = member.getMemberStatus();
    }
}
