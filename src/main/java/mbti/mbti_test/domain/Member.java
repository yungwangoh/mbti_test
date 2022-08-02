package mbti.mbti_test.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String name;
    private String account; //회원 ACCOUNT
    private String pwd; //회원 PWD
    @Embedded
    private Address address;

    private String email;
    private LocalDateTime localDateTime;

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
    }

    public Member(String account, String pwd, Address address, String email) {
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
    }
}
