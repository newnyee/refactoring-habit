package com.refactoringhabit.member.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.member.domain.enums.Gender;
import com.refactoringhabit.member.domain.enums.MemberStatus;
import com.refactoringhabit.member.domain.enums.MemberType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private String email;
    private String password;
    private String nickName;
    private String phone;
    private String birth;
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Gender gender; // 'MALE', 'FEMALE'

    @Enumerated(EnumType.STRING)
    private MemberStatus status; // 회원 상태 - 'WITHDRAWN', 'ACTIVE', 'INACTIVE'

    @Enumerated(EnumType.STRING)
    private MemberType type; // 회원 구분 - 'MEMBER', 'HOST'
}
