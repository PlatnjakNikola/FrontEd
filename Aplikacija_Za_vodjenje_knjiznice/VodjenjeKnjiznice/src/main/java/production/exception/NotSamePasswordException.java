package production.exception;

import java.io.Serial;
import java.io.Serializable;

public class NotSamePasswordException extends Exception implements Serializable {
    @Serial
    private static final long serialVersionUID = -2397286408850832348L;

    public NotSamePasswordException() {
    }

    public NotSamePasswordException(String message) {
        super(message);
    }

    public NotSamePasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSamePasswordException(Throwable cause) {
        super(cause);
    }

    public NotSamePasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
