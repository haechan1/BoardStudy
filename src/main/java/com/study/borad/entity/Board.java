package com.study.borad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data //Data로 처리해서 필드들 읽을 수 있게?
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql,mariadb는 IDENTITY
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;

}
