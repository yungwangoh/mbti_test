package mbti.mbti_test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "아이디는 필수입니다.")
    private String account; //회원 ACCOUNT

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String pwd; //회원 PWD

    @Embedded
    private Address address;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateDateTime;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus; // 유저와 비유저 [USER, NONUSER]

    @OneToMany(mappedBy = "member")
    private List<Result> results = new ArrayList<>();

    public Member(String name, String account, String pwd, Address address, String email, MemberStatus memberStatus) {
        this.name = name;
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.memberStatus = memberStatus;
        this.createDateTime = LocalDateTime.now();
    }

    //0804 Hayoon Setter 삭제 후 updateMember 메서드 생성
    public void updateMember(String account, String pwd, Address address, String email, LocalDateTime updateDateTime) {
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.updateDateTime = updateDateTime;

        //회원일 경우-> 테스트 결과지 회원상태로 수정 후 공유가능?
        //result.setMemberStatus(MemberStatus.MEMBER);
    }
}
