package com.example.lms.vo;

import lombok.Builder;
import lombok.Data;


/**
 * @author 王子昂
 * @date 5/19/21
 * @description 用户解析后信息
 */
@Data
@Builder
public class UserVo {

    /**
     * 用户id
     */
    private int userId;

}
