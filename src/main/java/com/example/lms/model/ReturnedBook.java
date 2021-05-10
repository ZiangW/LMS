package com.example.lms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BSM.returnbook")
@Getter
@Setter
public class ReturnedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer returnId;

    private Integer returnUserId;

    private Integer returnBookId;

    private Date returnDate;

    private Integer returnStatus;

}
