package com.example.lms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "BSM.admin")
public class Admin {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Getter
    @Setter
    private String adminName;

    @Getter
    @Setter
    private String adminPwd;

    @Getter
    @Setter
    private String adminEmail;

}
