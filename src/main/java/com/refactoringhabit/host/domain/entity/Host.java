package com.refactoringhabit.host.domain.entity;

import com.refactoringhabit.common.domain.entity.BaseTimeEntity;
import com.refactoringhabit.member.domain.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "hosts")
@Entity
public class Host extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String altId; // 대체키
    private String nickName;
    private String phone;
    private String profileImage;
    private String email;
    private String introduction; // 호스트 소개글
    private String accountNumber; // 정산 계좌 번호
    private String bank; // 정산 은행 이름
    private String accountHolder; // 예금주

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
