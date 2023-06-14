package vn.com.hugio.common.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InternalServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -9124555485462458219L;

    private String code;
    private String codeMessage;

    public InternalServiceException() {
    }

    public InternalServiceException(String code, String codeMessage) {
        super(codeMessage);
        this.code = code;
        this.codeMessage = codeMessage;
    }

    public InternalServiceException(int code, String codeMessage) {
        super(codeMessage);
        this.code = String.valueOf(code);
        this.codeMessage = codeMessage;
    }

    public InternalServiceException(long code, String codeMessage) {
        super(codeMessage);
        this.code = String.valueOf(code);
        this.codeMessage = codeMessage;
    }

    public InternalServiceException(HttpStatus code, String codeMessage) {
        super(codeMessage);
        this.code = String.valueOf(code.value());
        this.codeMessage = codeMessage;
    }

    public InternalServiceException(Exception e) {
        super(e);
        this.code = "-1";
        this.codeMessage = e.getMessage();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
    }
}
