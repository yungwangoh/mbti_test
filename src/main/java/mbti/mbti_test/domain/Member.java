package mbti.mbti_test.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
// ㅎㅇ11
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus; // 유저와 비유저 [USER, NONUSER]

    @OneToMany(mappedBy = "member")
    private List<Result> result = new ArrayList<>();

    public Member(String name, Address address, String email, MemberStatus memberStatus) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.memberStatus = memberStatus;
    }
}
