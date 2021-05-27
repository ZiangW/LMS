package com.example.lms.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王子昂
 * @date 5/19/21
 * @description 图书解析后信息
 */
@Data
@Builder
public class BookVo {

    /**
     * 图书id
     */
    private int bookId;

}
