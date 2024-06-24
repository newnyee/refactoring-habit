package com.refactoringhabit.host.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "hosts")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Host extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_id")
    private String altId; // 대체키

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "email")
    private String email;

    @Column(name = "introduction")
    private String introduction; // 호스트 소개글

    @Column(name = "account_number")
    private String accountNumber; // 정산 계좌 번호

    @Column(name = "bank")
    private String bank; // 정산 은행 코드 번호

    @Column(name = "account_holder")
    private String accountHolder; // 예금주

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Host(String altId, String nickName, String phone, String profileImage, String email,
        String introduction, String accountNumber, String bank, String accountHolder,
        Member member) {

        this.altId = altId;
        this.nickName = nickName;
        this.phone = phone;
        this.profileImage = profileImage;
        this.email = email;
        this.introduction = introduction;
        this.accountNumber = accountNumber;
        this.bank = bank;
        this.accountHolder = accountHolder;
        this.member = member;
    }
}
