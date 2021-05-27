package com.example.lms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wza
 * @date 5/19/21
 * @description 错误码枚举
 */
@AllArgsConstructor
@Getter
public enum RespEnum {
    /**
     * 成功
     */
    OK(20000, ""),
    SERVICE_ERROR(20001, "业务处理报错"),
    PARAM_ERROR(20002, "参数校验报错"),
    AUTH_ERROR(20003, "权限验证报错"),
    REMOTE_ERROR(20004, "远程调用报错"),
    EMPTY_RESULT(20005, "空结果报错"),
    RECORD_ERROR(20006, "记录报错"),
    FLOW_ERROR(20007, "业务流程报错"),
    RESOURCE_NOT_FOUND(20008, "资源查询报错"),
    BUSINESS_CHECK_ERROR(20009, "业务检查报错"),
    BEAN_INT_ERROR(20010, "初始化异常"),
    REPEAT_ERROR(20011, "重复请求");

    private Integer errno;

    private String errMsg;
}
