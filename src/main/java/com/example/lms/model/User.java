package com.example.lms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "BSM.user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String userName;

    private String userPwd;

    private String userEmail;

    private String userType;

    private Integer adminId;

    private Integer userStatus;

}
