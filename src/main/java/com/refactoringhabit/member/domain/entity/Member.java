package com.refactoringhabit.member.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.member.domain.enums.Gender;
import com.refactoringhabit.member.domain.enums.MemberStatus;
import com.refactoringhabit.member.domain.enums.MemberType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Table(name = "members")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String encodedPassword;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "birth")
    private String birth;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status; // default 'ACTIVE'

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MemberType type; // default 'MEMBER'

    @Builder
    public Member(String altId, String email, String password, String nickName, String phone,
        String birth, String profileImage, Gender gender) {

        this.altId = altId;
        this.email = email;
        this.encodedPassword = password;
        this.nickName = nickName;
        this.phone = phone;
        this.birth = birth;
        this.profileImage = profileImage;
        this.gender = gender;
    }

    public void changePassword(String password) {
        this.encodedPassword = password;
    }
}
