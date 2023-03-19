package production.exception;

import java.io.Serial;
import java.io.Serializable;

public class UnknownUserException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 7557802455164984553L;

    public UnknownUserException() {
    }

    public UnknownUserException(String message) {
        super(message);
    }

    public UnknownUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownUserException(Throwable cause) {
        super(cause);
    }

    public UnknownUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
