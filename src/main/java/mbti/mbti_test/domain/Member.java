package mbti.mbti_test.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor //0806 Hayoon
@Builder
@Entity
public class Member implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "아이디는 필수입니다.")
    @Column(length = 100, nullable = false, unique = true)
    private String account; //회원 ACCOUNT

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Column(length = 300, nullable = false)
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

    //0806 Hayoon
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public Member(String name, String account, String pwd, Address address, String email, MemberStatus memberStatus) {
        this.name = name;
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.memberStatus = memberStatus;
        this.createDateTime = LocalDateTime.now();
    }

    //0804 Hayoon
    //updateMember 메서드 생성
    public void updateMember(String account, String pwd, Address address, String email) {
        this.account = account;
        this.pwd = pwd;
        this.address = address;
        this.email = email;
        this.updateDateTime = LocalDateTime.now();

        //회원일 경우-> 테스트 결과지 회원상태로 수정 후 공유가능?
        //result.setMemberStatus(MemberStatus.MEMBER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() { //회원 아이디(계정) == username
        return account;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
