package com.example.lms.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王子昂
 * @date 5/19/21
 * @description 管理员解析后信息
 */
@Data
@Builder
public class AdminVo {

    /**
     * 管理员id
     */
    private Integer adminId;

}
