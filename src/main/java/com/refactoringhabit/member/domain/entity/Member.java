package com.refactoringhabit.member.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "members")
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mid; // 외부 이용 식별자
    private String userId; // 가입 아이디
    private String password;
    private String name;
    private String email;
    private String phone;
    private String birth;
    private Enum gender; //	'MALE', 'FEMALE'
    private String profileImage;
    private Enum status; // 회원 상태 - 'WITHDRAWN', 'ACTIVE', 'INACTIVE'
    private Enum type; // 회원 구분 - 'MEMBER', 'HOST'
}
