package vn.com.hugio.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionStackTraceUtil {

    public static String getStackTrace(Throwable throwable) {
        try {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            throwable.printStackTrace(printWriter);
            return result.toString();
        } catch (Exception ex) {
            return "Null pointer Exception";
        }
    }

}
