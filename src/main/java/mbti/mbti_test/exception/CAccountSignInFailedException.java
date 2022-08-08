package mbti.mbti_test.exception;

public class CAccountSignInFailedException extends RuntimeException {

    public CAccountSignInFailedException() {
    }

    public CAccountSignInFailedException(String message) {
        super(message);
    }

    public CAccountSignInFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CAccountSignInFailedException(Throwable cause) {
        super(cause);
    }

    public CAccountSignInFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
