package com.example.lms.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description The entity of BookCategory.
 */
@Table(name = "BSM.book_category")
@Data
public class BookCategory implements Serializable {

    /**
     * 图书分类id
     */
    @Min(value = 1, message = "图书分类id必须大于0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类状态
     */
    @Range(min = 0, max = 1, message = "状态只能为0或1")
    private Integer categoryStatus;

}
