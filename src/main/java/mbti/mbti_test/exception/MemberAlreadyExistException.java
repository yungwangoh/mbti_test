package mbti.mbti_test.exception;

public class MemberAlreadyExistException extends Throwable {

    public MemberAlreadyExistException() {
    }

    public MemberAlreadyExistException(String message) {
        super(message);
    }

    public MemberAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public MemberAlreadyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
