package com.example.lms.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description The entity of User.
 */
@Table(name = "BSM.admin")
@Data
public class Admin implements Serializable {

    /**
     * 管理员id
     */
    @Min(value = 1, message = "管理员id必须大于0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    /**
     * 管理员名
     */
    @Size(max = 10, min = 1, message = "名称长度应在1-10")
    private String adminName;

    /**
     * 管理员密码
     */
    private String adminPwd;

    /**
     * 管理员邮箱
     */
    @Email(message = "邮箱格式有误")
    private String adminEmail;

    /**
     * 管理员状态
     */
    @Range(min = 0, max = 1, message = "状态只能为0或1")
    private Integer adminStatus;

}
