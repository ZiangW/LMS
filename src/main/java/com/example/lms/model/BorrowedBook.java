package com.example.lms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BSM.lendbook")
@Getter
@Setter
public class BorrowedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lendId;

    private Integer lendUserId;

    private Integer lendBookId;

    private Date lendDate;

    private Integer lendStatus;

}
