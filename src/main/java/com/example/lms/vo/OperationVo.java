package com.example.lms.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王子昂
 * @date 5/19/21
 * @description 操作结果解析后信息
 */
@Data
@Builder
public class OperationVo {

    /**
     * 操作code
     */
    private int operationCode;

}
