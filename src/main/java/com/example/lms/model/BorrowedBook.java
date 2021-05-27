package com.example.lms.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description 借阅记录
 */
@Table(name = "BSM.lendbook")
@Data
public class BorrowedBook {

    /**
     * 借阅id
     */
    @Min(value = 1, message = "借阅id必须大于0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lendId;

    /**
     * 用户id
     */
    @Min(value = 1, message = "用户id必须大于0")
    private Integer lendUserId;

    /**
     * 图书id
     */
    @Min(value = 1, message = "图书id必须大于0")
    private Integer lendBookId;

    /**
     * 借阅日期
     */
    private Timestamp lendDate;

    /**
     * 借阅状态
     */
    @Range(min = 0, max = 1, message = "状态只能为0或1")
    private Integer lendStatus;

}
