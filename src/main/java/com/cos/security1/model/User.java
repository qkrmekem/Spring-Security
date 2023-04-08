package com.cos.security1.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // ROLE_USER, ROLE_ADMIN

    // provider / providerId 추가
    private String provider; // 값을 받아온 도메인('google')
    private String providerId; // 도메인에서 사용하는 id('sub값')

    private Timestamp loginDate; // 로그인 한 날짜
    @CreationTimestamp
    private Timestamp createDate;
}
