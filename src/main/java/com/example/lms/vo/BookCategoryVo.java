package com.example.lms.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王子昂
 * @date 5/19/21
 * @description 图书分类解析后信息
 */
@Data
@Builder
public class BookCategoryVo {

    /**
     * 分类id
     */
    private int categoryId;

}
