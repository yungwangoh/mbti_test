package mbti.mbti_test.dto;

import lombok.Data;
import mbti.mbti_test.domain.Address;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

//0803 hayoon
@Data
public class UpdateMemberDto {

    private String email;
    private Address address;

    @NotEmpty(message = "아이디는 필수입니다.")
    private String account;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String pwd;

    private LocalDateTime updateDateTime;
    //private List<MemberFileDto> fileList;


    public UpdateMemberDto(String email, Address address,
                           String account, String pwd, Long creatorId,
                           LocalDateTime updateDateTime) {
        this.email = email;
        this.address = address;
        this.account = account;
        this.pwd = pwd;
        this.updateDateTime = updateDateTime;
    }
}
