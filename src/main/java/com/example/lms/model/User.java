package com.example.lms.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author 王子昂
 * @date 5/21/21
 * @description The entity of User.
 */
@Table(name = "BSM.user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 用户id
     */
    @Min(value = 1, message = "用户id必须大于0")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * 用户名
     */
    @Size(max = 10, min = 1, message = "名称长度应为1-10")
    private String userName;

    /**
     * 用户密码
     */
    @Size(max = 16, min = 8, message = "密码长度应为8-16")
    private String userPwd;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式错误")
    private String userEmail;

    /**
     * 用户类型
     */
    @Size(max = 16, min = 1, message = "用户类型长度应为1-16")
    private String userType;

    /**
     * 管理员id
     */
    @Min(value = 1, message = "管理员id必须大于0")
    private Integer adminId;

    /**
     * 用户状态
     */
    @Range(min = 0, max = 1, message = "用户状态只能为0或1")
    private Integer userStatus;

}
