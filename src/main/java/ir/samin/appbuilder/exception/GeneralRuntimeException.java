package ir.samin.appbuilder.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class GeneralRuntimeException extends RuntimeException{

    private HttpStatus status;
    private List<String> causes;

    public GeneralRuntimeException() {
    }

    public GeneralRuntimeException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
        causes = Collections.singletonList(message);
    }

    public GeneralRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralRuntimeException(String message, List<String> causes) {
        super(message);
        this.causes = causes;
        status = HttpStatus.BAD_REQUEST;
    }

    public GeneralRuntimeException(String message, HttpStatus status, List<String> causes) {
        super(message);
        this.status = status;
        this.causes = causes;
    }

    public GeneralRuntimeException(String message, HttpStatus status, String cause) {
        super(message);
        this.status = status;
        this.causes = Collections.singletonList(cause);
    }


    public List<String> getCauses() {
        return causes;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
