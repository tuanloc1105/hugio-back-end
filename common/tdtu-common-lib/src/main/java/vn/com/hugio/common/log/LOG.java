package vn.com.hugio.common.log;

import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class LOG {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

    private static void handleInternal(Date dateCreated, String clientIP, String httpMethod, String traceId,
                                       String operatorName, String stepName, String serviceDomain, AdapterHandler adapter) {
        MDC.put("X-B2-TraceId", traceId);
        MDC.put("clientIP", clientIP);
        MDC.put("httpMethod", httpMethod);
        MDC.put("operatorName", operatorName);
        MDC.put("stepName", stepName);
        MDC.put("serviceDomain", serviceDomain);
        MDC.put("dateCreated", sdf.format(dateCreated));

        adapter.execute();

        MDC.remove("X-B2-TraceId");
        MDC.remove("clientIP");
        //MDC.remove("httpMethod");
        MDC.remove("operatorName");
        MDC.remove("stepName");
        MDC.remove("dateCreated");
        MDC.remove("serviceDomain");
    }

    private static void handleInternal(Date dateCreated, String clientIP, String httpMethod, String traceId,
                                       String operatorName, String stepName, String serviceDomain, Map<String, String> extraParams, AdapterHandler adapter) {
        if (extraParams != null) {
            for (Entry<String, String> entry : extraParams.entrySet()) {
                MDC.put(entry.getKey(), entry.getValue());
            }
        }

        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, adapter);

        if (extraParams != null) {
            for (Entry<String, String> entry : extraParams.entrySet()) {
                MDC.remove(entry.getKey());
            }
        }
    }

    public static void info(String msg, Object... param) {
        if (msg.contains("{}")) {
            msg = msg.replace("{}", "%s");
        }
        List<StackTraceElement> asd = Arrays.asList(Thread.currentThread().getStackTrace());
        String className = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClassName();
        String methodName = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getMethodName();
        int line = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getLineNumber();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (Exception ignored) {
            clazz = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClass();
        }
        info(
                new Date(),
                clazz,
                MDC.get("traceId"),
                methodName,
                String.valueOf(line),
                clazz.getSimpleName(),
                param.length > 0 ? String.format(msg, param) : msg
        );
    }

    public static void warn(String msg, Object... param) {
        if (msg.contains("{}")) {
            msg = msg.replace("{}", "%s");
        }
        List<StackTraceElement> asd = Arrays.asList(Thread.currentThread().getStackTrace());
        String className = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClassName();
        String methodName = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getMethodName();
        int line = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getLineNumber();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (Exception ignored) {
            clazz = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClass();
        }
        warn(
                new Date(),
                clazz,
                MDC.get("traceId"),
                methodName,
                String.valueOf(line),
                clazz.getSimpleName(),
                param.length > 0 ? String.format(msg, param) : msg
        );
    }

    public static void error(String msg, Object... param) {
        if (msg.contains("{}")) {
            msg = msg.replace("{}", "%s");
        }
        List<StackTraceElement> asd = Arrays.asList(Thread.currentThread().getStackTrace());
        String className = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClassName();
        String methodName = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getMethodName();
        int line = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getLineNumber();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (Exception ignored) {
            clazz = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClass();
        }
        error(
                new Date(),
                clazz,
                MDC.get("traceId"),
                methodName,
                String.valueOf(line),
                clazz.getSimpleName(),
                param.length > 0 ? String.format(msg, param) : msg
        );
    }

    public static void exception(Throwable e) {
        List<StackTraceElement> asd = Arrays.asList(Thread.currentThread().getStackTrace());
        String className = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClassName();
        String methodName = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getMethodName();
        int line = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getLineNumber();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (Exception ignored) {
            clazz = Arrays.asList(Thread.currentThread().getStackTrace()).get(2).getClass();
        }
        error(
                new Date(),
                clazz,
                MDC.get("traceId"),
                methodName,
                String.valueOf(line),
                clazz.getSimpleName(),
                Optional.ofNullable(e.getMessage()).isPresent() ? e.getMessage() : "Null pointer"
        );
    }

    public static void info(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            String message) {
        info(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message);
    }

    public static void info(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            String message, Map<String, String> extraParams) {
        info(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message, extraParams);
    }

    public static void info(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            Exception ex) {
        info(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex);
    }

    public static void info(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            Exception ex, Map<String, String> extraParams) {
        info(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex, extraParams);
    }

    public static void debug(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             String message) {
        debug(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message);
    }

    public static void debug(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             String message, Map<String, String> extraParams) {
        debug(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message, extraParams);
    }

    public static void debug(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             Exception ex) {
        debug(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex);
    }

    public static void debug(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             Exception ex, Map<String, String> extraParams) {
        debug(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex, extraParams);
    }

    public static void error(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             String message) {
        error(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message);
    }

    public static void error(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             String message, Map<String, String> extraParams) {
        error(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message, extraParams);
    }

    public static void error(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             Exception ex) {
        error(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex);
    }

    public static void error(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                             Exception ex, Map<String, String> extraParams) {
        error(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex, extraParams);
    }

    public static void warn(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            String message) {
        warn(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message);
    }

    public static void warn(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            String message, Map<String, String> extraParams) {
        warn(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, message, extraParams);
    }

    public static void warn(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            Exception ex) {
        warn(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex);
    }

    public static void warn(Date dateCreated, Class<?> clazz, String traceId, String operatorName, String stepName, String serviceDomain,
                            Exception ex, Map<String, String> extraParams) {
        warn(dateCreated, null, null, clazz, traceId, operatorName, stepName, serviceDomain, ex, extraParams);
    }

    public static void info(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, String message) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).info(message));
    }

/////////////////////////////////////////////////////////////////////

    public static void info(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, String message, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).info(message));
    }

    public static void info(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, Exception ex) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).info(ex.getMessage(), ex));
    }

    public static void info(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, Exception ex, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).info(ex.getMessage(), ex));
    }

    public static void debug(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, String message) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).debug(message));
    }

    public static void debug(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, String message, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).debug(message));
    }

    public static void debug(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, Exception ex) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).debug(ex.getMessage(), ex));
    }

    public static void debug(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, Exception ex, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).debug(ex.getMessage(), ex));
    }

    public static void error(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, String message) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).error(message));
    }

    public static void error(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, String message, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).error(message));
    }

    public static void error(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, Exception ex) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).error(ex.getMessage(), ex));
    }

    public static void error(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                             String operatorName, String stepName, String serviceDomain, Exception ex, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).error(ex.getMessage(), ex));
    }

    public static void warn(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, String message) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).warn(message));
    }

    public static void warn(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, String message, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).warn(message));
    }

    public static void warn(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, Exception ex) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain,
                () -> LoggerFactory.getLogger(clazz).warn(ex.getMessage(), ex));
    }

    public static void warn(Date dateCreated, String clientIP, String httpMethod, Class<?> clazz, String traceId,
                            String operatorName, String stepName, String serviceDomain, Exception ex, Map<String, String> extraParams) {
        handleInternal(dateCreated, clientIP, httpMethod, traceId, operatorName, stepName, serviceDomain, extraParams,
                () -> LoggerFactory.getLogger(clazz).warn(ex.getMessage(), ex));
    }

    private interface AdapterHandler {
        void execute();
    }
}
