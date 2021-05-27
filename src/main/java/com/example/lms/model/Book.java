package com.example.lms.model;


import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;


/**
 * @author 王子昂
 * @date 5/21/21
 * @description The entity of Book.
 */
@Table(name = "BSM.book")
@Data
public class Book implements Serializable {

    /**
     * 图书id
     */
    @Min(value = 1, message = "图书id必须大于0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    /**
     * 图书名称
     */
    private String bookName;

    /**
     * 图书作者
     */
    private String bookAuthor;

    /**
     * 图书分类id
     */
    @Min(value = 1, message = "图书分类id必须大于0")
    private Integer bookCategory;

    /**
     * 图书余量
     */
    @PositiveOrZero(message = "图书余量大于等于0")
    private Integer bookCount;

    /**
     * 图书状态
     */
    @Range(min = 0, max = 1, message = "状态只能为0或1")
    private Integer bookStatus;

}
