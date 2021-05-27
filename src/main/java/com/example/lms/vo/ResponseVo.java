package com.example.lms.vo;

import com.example.lms.enums.RespEnum;
import lombok.Builder;
import lombok.Data;
import org.slf4j.MDC;

import static com.example.lms.constants.GlobalConstants.SESSION_TOKEN_ID;

/**
 * @author 王子昂
 * @date 5 /19/21
 * @description 返回结果解析后信息
 */
@Data
@Builder
public class ResponseVo<T>  {
    /**
     * 请求的logId 方便排查问题
     */
    private String traceId;

    /**
     * 错误码，具体参考RespEnum
     */
    private Integer errno;

    /**
     * 批次id
     */
    private T data;

    /**
     * 成功或者异常信息
     */
    private String message;

    /**
     * @param errno error number
     * @param message message
     * @return 返回错误结果
     */
    public static ResponseVo getErrorResp(Integer errno, String message) {
        return ResponseVo.builder()
                .errno(errno)
                .traceId(MDC.get(SESSION_TOKEN_ID))
                .message(message)
                .build();
    }


    /**
     * @param data
     * @return 返回成功结果
     */
    public static <T> ResponseVo<T> getSuccessResp(T data) {
        return ResponseVo.<T>builder()
                .errno(RespEnum.OK.getErrno())
                .data(data)
                .traceId(MDC.get(SESSION_TOKEN_ID))
                .message("成功")
                .build();
    }
}
