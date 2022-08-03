package mbti.mbti_test.Dto;

import lombok.Data;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.MemberStatus;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

//0803 hayoon
@Data
public class CreateMemberDto {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String email;

    @Embedded
    private Address address;

    @NotEmpty(message = "아이디는 필수입니다.")
    private String account;

    @NotEmpty(message = "비밀번호는 필수입니다.")
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
                           MemberStatus memberStatus, LocalDateTime createDateTime) {
        this.name = name;
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.memberStatus = memberStatus;
        this.createdDateTime = createDateTime;
    }
}
