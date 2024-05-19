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
    private Long id;
    private String altId; // 대체키
    private String email;
    private String password;
    private String nickName;
    private String phone;
    private String birth;
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberStatus status; // default 'ACTIVE'

    @Enumerated(EnumType.STRING)
    private MemberType type; // default 'MEMBER'

    @Builder
    public Member(String altId, String email, String password, String nickName, String phone,
        String birth, String profileImage, Gender gender) {

        this.altId = altId;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phone = phone;
        this.birth = birth;
        this.profileImage = profileImage;
        this.gender = gender;
    }
}
