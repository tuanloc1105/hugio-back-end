package vn.com.hugio.common.exceptions;

public interface ErrorCode {

    String getErrorCode();

    String getErrorMessage(Object... param);

}
