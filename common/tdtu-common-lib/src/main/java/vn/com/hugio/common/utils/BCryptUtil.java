package vn.com.hugio.common.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;

public class BCryptUtil {

    private volatile static BCryptUtil INSTANCE;

    public BCryptUtil() {
    }

    public synchronized BCryptUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BCryptUtil();
        }
        return INSTANCE;
    }

    private static final Integer[] HASH_ROUND = new Integer[]{4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    public static String hashPassword(String password) {
        if (StringUtil.isNotEmpty(password)) {
            return BCrypt.hashpw(password, BCrypt.gensalt(HASH_ROUND[0]));
        }
        throw new InternalServiceException(ErrorCodeEnum.VALIDATE_FAILURE.getCode(), "Password can not be null");
    }

    public static void comparePassword(String password, String passHash) {
        if (StringUtil.isNotEmpty(password) && StringUtil.isNotEmpty(password)) {
            var isMatch = BCrypt.checkpw(password, passHash);
            if (isMatch) {
                return;
            }
            throw new InternalServiceException(ErrorCodeEnum.AUTH_FAILURE.getCode(), "Password is not correct");
        }
        throw new InternalServiceException(ErrorCodeEnum.VALIDATE_FAILURE.getCode(), "password and passHash can not be null");
    }

}
