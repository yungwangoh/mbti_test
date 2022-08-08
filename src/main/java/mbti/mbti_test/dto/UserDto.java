package mbti.mbti_test.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private String account;
    private String password;

    public UserDto() {
    }

    public UserDto(String account, String password) {
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
