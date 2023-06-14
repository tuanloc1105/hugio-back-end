package vn.com.hugio.common.exceptions;

public enum ErrorCodeEnum {
	SUCCESS("Successful", 0),
	FAILURE("Failure", 1),
	SP_ERROR("Error in SP '%s' with resultCode = %s", 2),
	CANNOT_DECRYPT("Can not decrypt data", 3),
	CANNOT_ENCRYPT("Can not encrypt data", 4),
	FORMAT_ERROR("Format error", 5),
	CANNOT_CALL_SP("Can not call SP %s in Java", 6),
	EXISTS("Data Exists", 7),
	VALIDATE_FAILURE("Validate failure: %s", 8),
	NOT_EXISTS("Data Not Exists", 9),
	AUTH_FAILURE("Authenticate failure", 10),
	CANNOT_CONNECT_DB("Can not connect db %s in Java", 11),
	CANNOT_CLOSE_CONNECTION("Can not close connection db %s in Java", 12),
	USER_DISABLE("User disable", 92),
	XML_ERROR("Parse XML failure. Exception occur in %s method: %s", 93),
	PARSE_DATA_ERROR("Parse data error", 94),
	T24_ERROR("T24 failure. %s", 95),
	JSON_ERROR("Parse JSON failure. Exception occur in %s method: %s", 96),
	ESB_FAILURE("Exception occur in %s ESB method: %s",97),
	DB_FAILURE("Exception occur in %s DB method: %s",98),
	INTERNAL_ERROR("Exception. Process fail. %s", 99),
	LOGIC_ERROR("Logic error in code java", 100),
	UNEXPECTED_ERROR("Unexpected Error.", 101),
	DEVICE_LOCKED("Device Locked", 102),
	IN_VALID_REQUEST_HEADER("Invalid Request Header", 103),
	DEVICE_REMOVED("Device Removed", 104),
	CAN_NOT_REMOVE_DEVICE("Can't Unregister Current Device", 105),
	DEVICE_NOT_EXIST("Device Is Not Existing", 106),
	CAN_NOT_LOCK_DEVICE("Can't Lock Current Device", 107);

	private String message;
	private Integer code;

	ErrorCodeEnum(String message, Integer code) {
		this.message = message;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public Integer getCode() {
		return code;
	}
	
}
