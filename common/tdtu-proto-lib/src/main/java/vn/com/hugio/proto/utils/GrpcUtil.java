package vn.com.hugio.proto.utils;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import vn.com.hugio.proto.common.TraceTypeGRPC;

public class GrpcUtil {

    private static final String HYPHEN = "-";

    public static TraceTypeGRPC createTraceTypeGrpc() {
        return TraceTypeGRPC
                .newBuilder()
                .setCid(MDC.get("cid"))
                .setSid(MDC.get("traceId"))
                .build();
    }

    public static void getTraceId(TraceTypeGRPC traceTypeGRPC) {
        if (
                !(StringUtils.hasText(traceTypeGRPC.getCid()))
                        || !(StringUtils.hasText(traceTypeGRPC.getSid()))
        ) {
            throw new RuntimeException("trace.cid and trace.sid is required");
        }
        MDC.put("cid", traceTypeGRPC.getCid());
        MDC.put("traceId", traceTypeGRPC.getSid());
    }

}
