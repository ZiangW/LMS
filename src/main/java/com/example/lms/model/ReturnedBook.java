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
 * @description 归还记录
 */
@Table(name = "BSM.returnbook")
@Data
public class ReturnedBook {

    /**
     * 归还id
     */
    @Min(value = 1, message = "归还id必须大于0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer returnId;

    /**
     * 用户id
     */
    @Min(value = 1, message = "用户id必须大于0")
    private Integer returnUserId;

    /**
     * 图书id
     */
    @Min(value = 1, message = "图书id必须大于0")
    private Integer returnBookId;

    /**
     * 归还日期
     */
    private Timestamp returnDate;

    /**
     * 归还状态
     */
    @Range(min = 0, max = 1, message = "状态只能为0或1")
    private Integer returnStatus;

}
