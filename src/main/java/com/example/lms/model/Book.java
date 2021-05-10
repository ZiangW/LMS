package com.example.lms.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name = "BSM.book")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    private String bookName;

    private String bookAuthor;

    private Integer bookCategory;

    private Integer bookCount;

    private Integer bookStatus;

}
