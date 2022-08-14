package mbti.mbti_test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;

import lombok.NoArgsConstructor;
import mbti.mbti_test.domain.Address;
import mbti.mbti_test.domain.Member;
import mbti.mbti_test.domain.MemberStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

//0803 hayoon
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class CreateMemberDto implements Serializable {

    @JsonIgnore
    private Long id;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String email;

    private Address address;

    @NotEmpty(message = "아이디는 필수입니다.")
    private String account;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    //0814 Hayoon
    // 전체회원 조회 시(Get Method), 회원 Password 비공개
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus; // 유저와 비유저 [USER, NONUSER]

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateDateTime;

    //private List<MemberFileDto> fileList;
    public CreateMemberDto(String name, String account, String pwd,
                           Address address, String email) { // 객체로 파라미터로 넘기는게 더 좋아보임.
        this.name = name;
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.memberStatus = MemberStatus.USER;
        this.createDateTime = LocalDateTime.now();
        this.updateDateTime = LocalDateTime.now();
    }

    public CreateMemberDto(Member member) { // createResultDto에 쓰일 CreateMemberDto 생성 0804.
        this.name = member.getName();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.account = member.getAccount();
        this.pwd = member.getPwd();
        this.memberStatus = member.getMemberStatus();
        this.updateDateTime = member.getUpdateDateTime();
        this.createDateTime = member.getCreateDateTime();
    }
}
