package mbti.mbti_test.dto;

import lombok.Getter;

@Getter
public class UserLoginDto {

    private String account;
    private String password;

    public UserLoginDto() {
    }

    public UserLoginDto(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
