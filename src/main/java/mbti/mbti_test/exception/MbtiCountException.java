package mbti.mbti_test.exception;

//0803 hayoon
public class MbtiCountException extends RuntimeException {

    public MbtiCountException() {
    }

    public MbtiCountException(String message) {
        super(message);
    }

    public MbtiCountException(String message, Throwable cause) {
        super(message, cause);
    }

    public MbtiCountException(Throwable cause) {
        super(cause);
    }
}
