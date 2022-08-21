package mbti.mbti_test.config.security.user;

import mbti.mbti_test.domain.Member;
import org.springframework.security.core.userdetails.User;

public class MemberAdapter extends User{ //

    private Member member;
    public MemberAdapter(Member member) {
        super(member.getAccount(), member.getPassword(), member.getAuthorities());
        this.member = member;
    }
}
